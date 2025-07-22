import os
import json
from flask import Flask, request, jsonify
from flask_cors import CORS
from slack_sdk import WebClient
from slack_sdk.errors import SlackApiError

app = Flask(__name__)
CORS(app)

slack_token = os.environ.get("SLACK_BOT_TOKEN")
slack_channel = "#help-domain-name"
slack_client = WebClient(token=slack_token)

@app.route('/ask-in-slack', methods=['POST'])
def ask_in_slack():
    try:
        data = request.json
        
        if not data:
            return jsonify({"error": "No data provided"}), 400
        
        username = data.get('username')
        message = data.get('message')
        link = data.get('link')
        
        if not username or not message:
            return jsonify({"error": "Missing required fields: username and message"}), 400
        
        slack_message = format_slack_message(username, message, link)
        
        result = slack_client.chat_postMessage(
            channel=slack_channel,
            blocks=slack_message,
            text=f"Question from {username}"
        )
        
        return jsonify({"success": True, "ts": result['ts']}), 200
        
    except SlackApiError as e:
        return jsonify({"error": f"Slack API error: {e.response['error']}"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

def format_slack_message(username, message, link=None):
    blocks = [{
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": f"<@{username}> asked a question:"
        }
    }, {"type": "divider"}]

    parts = parse_message_with_code(message)
    
    for part in parts:
        if part['type'] == 'text':
            blocks.append({
                "type": "section",
                "text": {
                    "type": "mrkdwn",
                    "text": part['content']
                }
            })
        elif part['type'] == 'code':
            blocks.append({
                "type": "section",
                "text": {
                    "type": "mrkdwn",
                    "text": f"```{part.get('language', '')}\n{part['content']}\n```"
                }
            })
    
    if link:
        blocks.append({"type": "divider"})
        blocks.append({
            "type": "section",
            "text": {
                "type": "mrkdwn",
                "text": f"<{link}|View more details>"
            }
        })
    
    return blocks

def parse_message_with_code(message):
    parts = []
    
    if isinstance(message, str):
        parts.append({"type": "text", "content": message})
    elif isinstance(message, list):
        for item in message:
            if isinstance(item, dict):
                parts.append(item)
            else:
                parts.append({"type": "text", "content": str(item)})
    elif isinstance(message, dict):
        if 'type' in message and 'content' in message:
            parts.append(message)
        else:
            parts.append({"type": "text", "content": json.dumps(message)})
    
    return parts

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5111, debug=True)