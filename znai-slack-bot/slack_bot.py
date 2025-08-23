import os
import json
from flask import Flask, request, jsonify
from flask_cors import CORS
from slack_sdk import WebClient
from slack_sdk.errors import SlackApiError

app = Flask(__name__)
CORS(app)

slack_token = os.environ.get("SLACK_BOT_TOKEN")

print("=== Slack Bot Configuration ===")
print(f"SLACK_BOT_TOKEN present: {slack_token is not None}")
print(f"SLACK_BOT_TOKEN length: {len(slack_token) if slack_token else 0}")
print(f"SLACK_BOT_TOKEN prefix: {slack_token[:10] if slack_token else 'None'}...")

if not slack_token:
    print("WARNING: SLACK_BOT_TOKEN environment variable is not set!")

slack_client = WebClient(token=slack_token)

@app.route('/ask-in-slack', methods=['POST'])
def ask_in_slack():
    try:
        print("=== ask-in-slack request received ===")
        
        if not slack_token:
            print("ERROR: No Slack token configured")
            return jsonify({"error": "Slack bot token not configured"}), 500
            
        # Get JSON data from request
        data = request.get_json()

        if not data:
            return jsonify({"error": "No JSON data provided"}), 400
        
        selected_text = data.get('selectedText')
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
        
        print(f"Message posted successfully: {result['ts']}")
        return jsonify({"success": True, "ts": result['ts']}), 200
        
    except SlackApiError as e:
        print(f"SlackApiError in main handler: {e.response}")
        return jsonify({"error": f"Slack API error: {e.response['error']}"}), 500
    except Exception as e:
        print(f"Unexpected error in main handler: {type(e).__name__}: {str(e)}")
        import traceback
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500


def format_slack_message(username, question, context, page_url):
    # Build message text with "asked" as the link
    message_parts = [f"User *{username}* <{page_url}|asked>: {question}"]
    
    if context:
        message_parts.append(context)
    
    # Return as single text message without sections for full width
    return "\n\n".join(message_parts)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5111, debug=True)