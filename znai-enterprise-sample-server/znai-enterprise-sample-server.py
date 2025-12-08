import os
import json
import csv
import uuid
from datetime import datetime
from flask import Flask, request, jsonify
from flask_cors import CORS
from slack_sdk import WebClient
from slack_sdk.errors import SlackApiError

# This is a NON production slack bot app to test znai slack integration
# You most likely need to implement your own bot with a proper storage story

app = Flask(__name__)
CORS(app, supports_credentials=True, origins="*")

slack_token = os.environ.get("SLACK_BOT_TOKEN")

print("=== Slack Bot Configuration ===")
print(f"SLACK_BOT_TOKEN present: {slack_token is not None}")
print(f"SLACK_BOT_TOKEN length: {len(slack_token) if slack_token else 0}")
print(f"SLACK_BOT_TOKEN prefix: {slack_token[:10] if slack_token else 'None'}...")

if not slack_token:
    print("WARNING: SLACK_BOT_TOKEN environment variable is not set!")

slack_client = WebClient(token=slack_token)

CSV_FILE = "active_questions.csv"
CSV_HEADERS = ["id", "timestamp", "pageId", "pageUrl", "context", "selectedText", "selectedPrefix", "selectedSuffix", "question", "slackLink", "username", "channel", "slackMessageTs", "resolved"]

TRACKING_CSV_FILE = "tracking_events.csv"
TRACKING_CSV_HEADERS = ["docId", "pageId", "timestamp", "eventType",  "data"]

@app.route('/ask-in-slack', methods=['POST'])
def ask_in_slack():
    try:
        print("=== ask-in-slack request received ===")
        
        if not slack_token:
            print("ERROR: No Slack token configured")
            return jsonify({"error": "Slack bot token not configured"}), 500
            
        data = request.get_json()

        if not data:
            return jsonify({"error": "No JSON data provided"}), 400


        question_id = str(uuid.uuid4())
        page_id = data.get('pageId')
        page_origin = data.get('pageOrigin')
        page_url = f"{page_origin}/{page_id}/?questionId={question_id}"
        selected_text = data.get('selectedText')
        selected_prefix = data.get('selectedPrefix', '')
        selected_suffix = data.get('selectedSuffix', '')
        username = data.get('username')
        slack_channel = data.get('slackChannel')
        question = data.get('question')
        context = data.get('context')

        print(f"Selected text: {selected_text[:100] if selected_text else None}...")
        print(f"Page ID: {page_id}")
        print(f"Page URL: {page_url}")
        print(f"Username: {username}")
        print(f"Slack channel: {slack_channel}")
        print(f"Question: {question[:100] if question else None}...")
        print(f"Context: {context[:100] if context else None}...")

        if not question:
            return jsonify({"error": "Missing required field: question"}), 400
        
        if not slack_channel:
            return jsonify({"error": "Missing required field: slackChannel"}), 400
        
        slack_message = format_slack_message(username, question, context, page_url)
        print(f"Slack message blocks: {json.dumps(slack_message, indent=2)}")
        
        result = slack_client.chat_postMessage(
            channel=slack_channel,
            text=slack_message
        )
        
        message_ts = result['ts']
        slack_link = f"https://slack.com/archives/{slack_channel}/p{message_ts.replace('.', '')}"
        
        persist_questions_to_csv([{
            'id': question_id,
            'pageId': page_id,
            'context': context or '',
            'selectedText': selected_text or '',
            'selectedPrefix': selected_prefix,
            'selectedSuffix': selected_suffix,
            'question': question,
            'slackLink': slack_link,
            'channel': slack_channel,
            'slackMessageTs': message_ts,
            'resolved': False,
        }])
        
        print(f"Message posted successfully: {message_ts}")
        return jsonify({"success": True, "ts": message_ts, "slack_link": slack_link}), 200
        
    except SlackApiError as e:
        print(f"SlackApiError in main handler: {e.response}")
        return jsonify({"error": f"Slack API error: {e.response['error']}"}), 500
    except Exception as e:
        print(f"Unexpected error in main handler: {type(e).__name__}: {str(e)}")
        import traceback
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500


def persist_questions_to_csv(questions, mode='a'):
    file_exists = os.path.exists(CSV_FILE)
    
    with open(CSV_FILE, mode, newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=CSV_HEADERS)
        
        if mode == 'w' or (mode == 'a' and not file_exists):
            writer.writeheader()

        for question in questions:
            writer.writerow(question)

def load_questions_from_csv():
    if not os.path.exists(CSV_FILE):
        return []
    
    questions = []
    with open(CSV_FILE, 'r', newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            row['resolved'] =  row['resolved'].lower() == 'true'
            questions.append(row)

    return questions

def update_question_completion_status(message_ts, resolved):
    questions = load_questions_from_csv()
    updated = False
    
    for question in questions:
        if question['slackMessageTs'] == message_ts:
            question['resolved'] = resolved
            updated = True
            break
    
    if updated:
        persist_questions_to_csv(questions, mode='w')
    
    return updated

def check_slack_message_completion(channel, message_ts):
    try:
        result = slack_client.reactions_get(
            channel=channel,
            timestamp=message_ts
        )
        
        if 'message' in result and 'reactions' in result['message']:
            reactions = result['message']['reactions']
            for reaction in reactions:
                if reaction['name'] in ['white_check_mark', 'heavy_check_mark', 'resolved', 'done']:
                    return True
        
        return False
    except SlackApiError as e:
        print(f"Error checking reactions for {message_ts}: {e}")
        return False

@app.route('/active-questions', methods=['GET'])
def get_active_questions():
    try:
        page_id = request.args.get('pageId')
        question_id = request.args.get('questionId')
        questions = load_questions_from_csv()
        questions = [q for q in questions if q.get('pageId') == page_id and (q.get('resolved') == False or q.get('id') == question_id)]
        
        return jsonify(questions), 200
        
    except Exception as e:
        print(f"Error getting active questions: {type(e).__name__}: {str(e)}")
        return jsonify({"error": str(e)}), 500

@app.route('/resolve-slack-question/<ts>', methods=['POST'])
def resolve_slack_question(ts):
    try:
        print(f"=== resolve-slack-question request received for ts: {ts} ===")

        updated = update_question_completion_status(ts, True)

        if updated:
            print(f"Successfully marked question with ts {ts} as resolved")
            return jsonify({"success": True, "message": f"Question {ts} marked as resolved"}), 200
        else:
            print(f"Question with ts {ts} not found")
            return jsonify({"error": f"Question with ts {ts} not found"}), 404

    except Exception as e:
        print(f"Error resolving question: {type(e).__name__}: {str(e)}")
        import traceback
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500

@app.route('/track-activity', methods=['POST'])
def track_event():
    try:
        data = request.get_json()

        if not data:
            return jsonify({"error": "No JSON data provided"}), 400

        doc_id = data.get('docId')
        event_type = data.get('eventType')
        page_id = data.get('pageId')
        event_data = data.get('data', {})

        if not event_type or not page_id:
            return jsonify({"error": "Missing required fields: eventType and pageId"}), 400

        timestamp = datetime.utcnow().isoformat()

        persist_tracking_event({
            'docId': doc_id,
            'pageId': page_id,
            'timestamp': timestamp,
            'eventType': event_type,
            'data': json.dumps(event_data) if event_data else ''
        })

        return jsonify({"success": True}), 200

    except Exception as e:
        print(f"Error tracking event: {type(e).__name__}: {str(e)}")
        import traceback
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500

def persist_tracking_event(event):
    file_exists = os.path.exists(TRACKING_CSV_FILE)

    with open(TRACKING_CSV_FILE, 'a', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=TRACKING_CSV_HEADERS)

        if not file_exists:
            writer.writeheader()

        writer.writerow(event)

def format_slack_message(username, question, context, page_url):
    message_parts = [f"@{username} <{page_url}|asked>: {question}"]

    if context:
        message_parts.append(context)

    return "\n\n".join(message_parts)

# Erase tracking CSV file on server start
if os.path.exists(TRACKING_CSV_FILE):
    os.remove(TRACKING_CSV_FILE)
    print(f"Erased existing tracking file: {TRACKING_CSV_FILE}")

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5111, debug=True)