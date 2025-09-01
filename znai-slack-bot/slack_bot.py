import os
import json
import csv
import threading
import time
from datetime import datetime, timedelta
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
CSV_HEADERS = ["timestamp", "page_url", "context", "selected_text", "selected_prefix", "selected_suffix", "question", "slack_link", "username", "channel", "message_ts", "completed"]

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
        
        selected_text = data.get('selectedText')
        selected_prefix = data.get('selectedPrefix', '')
        selected_suffix = data.get('selectedSuffix', '')
        page_url = data.get('pageUrl')
        username = data.get('username')
        slack_channel = data.get('slackChannel')
        question = data.get('question')
        context = data.get('context')

        print(f"Selected text: {selected_text[:100] if selected_text else None}...")
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
            'timestamp': datetime.now().isoformat(),
            'page_url': page_url,
            'context': context or '',
            'selected_text': selected_text or '',
            'selected_prefix': selected_prefix,
            'selected_suffix': selected_suffix,
            'question': question,
            'slack_link': slack_link,
            'username': username or 'anonymous',
            'channel': slack_channel,
            'message_ts': message_ts,
            'completed': False
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


def persist_questions_to_csv(questions):
    file_exists = os.path.exists(CSV_FILE)
    
    with open(CSV_FILE, 'a', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=CSV_HEADERS)
        
        if not file_exists:
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
            row['completed'] = row['completed'].lower() == 'true' if row['completed'] else False
            questions.append(row)
    
    return questions

def update_question_completion_status(message_ts, completed):
    questions = load_questions_from_csv()
    
    for question in questions:
        if question['message_ts'] == message_ts:
            question['completed'] = completed

    persist_questions_to_csv(questions)

def check_slack_message_completion(channel, message_ts):
    try:
        result = slack_client.reactions_get(
            channel=channel,
            timestamp=message_ts
        )
        
        if 'message' in result and 'reactions' in result['message']:
            reactions = result['message']['reactions']
            for reaction in reactions:
                if reaction['name'] in ['white_check_mark', 'heavy_check_mark', 'completed', 'done']:
                    return True
        
        return False
    except SlackApiError as e:
        print(f"Error checking reactions for {message_ts}: {e}")
        return False

@app.route('/active-questions', methods=['GET'])
def get_active_questions():
    try:
        questions = load_questions_from_csv()
        return jsonify({"questions": questions}), 200
        
    except Exception as e:
        print(f"Error getting active questions: {type(e).__name__}: {str(e)}")
        return jsonify({"error": str(e)}), 500

def format_slack_message(username, question, context, page_url):
    message_parts = [f"@{username} <{page_url}|asked>: {question}"]
    
    if context:
        message_parts.append(context)
    
    return "\n\n".join(message_parts)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5111, debug=True)