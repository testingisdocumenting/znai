import os
import json
from flask import Flask, request, jsonify
from flask_cors import CORS
from slack_sdk import WebClient
from slack_sdk.errors import SlackApiError

app = Flask(__name__)
CORS(app)

slack_token = os.environ.get("SLACK_BOT_TOKEN")
slack_channel = os.environ.get("SLACK_CHANNEL", "C096MM39HC2")

print("=== Slack Bot Configuration ===")
print(f"SLACK_BOT_TOKEN present: {slack_token is not None}")
print(f"SLACK_BOT_TOKEN length: {len(slack_token) if slack_token else 0}")
print(f"SLACK_BOT_TOKEN prefix: {slack_token[:10] if slack_token else 'None'}...")
print(f"Slack channel: {slack_channel}")

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
            
        print(f"Form data keys: {list(request.form.keys())}")
        print(f"Files: {list(request.files.keys())}")
        
        selected_text = request.form.get('selectedText')
        page_url = request.form.get('pageUrl')
        username = request.form.get('username', 'web-user')

        print(f"Selected text: {selected_text[:100] if selected_text else None}...")
        print(f"Page URL: {page_url}")
        print(f"Username: {username}")

        if not selected_text:
            return jsonify({"error": "Missing required field: selectedText"}), 400
        
        slack_message = format_slack_message(username, selected_text, page_url)
        print(f"Slack message blocks: {json.dumps(slack_message, indent=2)}")
        
        result = slack_client.chat_postMessage(
            channel=slack_channel,
            blocks=slack_message,
            text=f"Question from {username} about: {selected_text[:50]}..."
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


def format_slack_message(username, selected_text, page_url=None):
    blocks = [{
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": f"*{username}* selected text and asked for help:"
        }
    }, {"type": "divider"}]

    escaped_text = selected_text.replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
    blocks.append({
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": f"*Selected text:*\n```{escaped_text}```"
        }
    })
    
    if page_url:
        blocks.append({"type": "divider"})
        blocks.append({
            "type": "section",
            "text": {
                "type": "mrkdwn",
                "text": f"📄 <{page_url}|View documentation page>"
            }
        })
    
    return blocks

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5111, debug=True)