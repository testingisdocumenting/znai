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
        if not slack_token:
            return jsonify({"error": "Slack bot token not configured"}), 500
            
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
        error_code = e.response.get('error', 'unknown')
        if error_code == 'invalid_auth':
            return jsonify({"error": "Invalid Slack authentication. Check SLACK_BOT_TOKEN."}), 500
        elif error_code == 'not_in_channel':
            return jsonify({"error": f"Bot is not in channel {slack_channel}"}), 500
        return jsonify({"error": f"Slack API error: {error_code}"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/ask-in-slack-with-image', methods=['POST'])
def ask_in_slack_with_image():
    try:
        print("=== ask-in-slack-with-image request received ===")
        
        if not slack_token:
            print("ERROR: No Slack token configured")
            return jsonify({"error": "Slack bot token not configured"}), 500
            
        print(f"Form data keys: {list(request.form.keys())}")
        print(f"Files: {list(request.files.keys())}")
        
        selected_text = request.form.get('selectedText')
        page_url = request.form.get('pageUrl')
        username = request.form.get('username', 'web-user')
        image_file = request.files.get('image')
        
        print(f"Selected text: {selected_text[:100] if selected_text else None}...")
        print(f"Page URL: {page_url}")
        print(f"Username: {username}")
        print(f"Image file present: {image_file is not None}")
        
        if not selected_text:
            return jsonify({"error": "Missing required field: selectedText"}), 400
        
        image_url = None
        
        # Try to upload image if provided and if we have the scope
        if image_file:
            try:
                # Read the file data
                file_data = image_file.read()
                print(f"Uploading image: {image_file.filename}, actual size: {len(file_data)} bytes")
                
                # Reset the stream position if needed
                image_file.seek(0)
                
                image_response = slack_client.files_upload_v2(
                    channel=slack_channel,
                    file=file_data,
                    filename=image_file.filename or 'context-screenshot.png',
                    title='Context Screenshot',
                    initial_comment=None
                )
                
                print(f"Image upload response: {json.dumps(image_response, indent=2)}")
                
                # Get the URL for the image - try different fields
                if 'file' in image_response:
                    file_info = image_response['file']
                    image_url = file_info.get('url_private') or file_info.get('url_private_download') or file_info.get('permalink')
                    print(f"Using image URL: {image_url}")
                    print(f"Available URL fields: {[k for k in file_info.keys() if 'url' in k or 'link' in k]}")
                    
            except SlackApiError as e:
                print(f"SlackApiError during image upload: {e.response}")
                error_code = e.response.get('error', '')
                
                # Handle different Slack API errors
                if error_code == 'invalid_auth':
                    print("ERROR: Invalid Slack authentication. Check your SLACK_BOT_TOKEN.")
                elif error_code == 'missing_scope':
                    print("Warning: Cannot upload image due to missing Slack scope.")
                elif error_code == 'not_in_channel':
                    print(f"ERROR: Bot is not in channel {slack_channel}. Please invite the bot to the channel.")
                elif error_code == 'channel_not_found':
                    print(f"ERROR: Channel '{slack_channel}' not found. Make sure the channel exists and bot has access.")
                else:
                    print(f"Unexpected Slack error: {error_code}")
                
                # Continue without image for auth/scope errors
                if error_code not in ['invalid_auth', 'missing_scope']:
                    raise
            except Exception as e:
                print(f"Unexpected error during image upload: {type(e).__name__}: {str(e)}")
                import traceback
                traceback.print_exc()
        
        # Send message with or without image
        print(f"Preparing to send message with image_url: {image_url}")
        slack_message = format_slack_message_with_image(username, selected_text, page_url, image_url)
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

def format_slack_message_with_image(username, selected_text, page_url=None, image_url=None):
    blocks = [{
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": f"*{username}* selected text and asked for help:"
        }
    }, {"type": "divider"}]

    # Format selected text with proper escaping
    escaped_text = selected_text.replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
    blocks.append({
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": f"*Selected text:*\n```{escaped_text}```"
        }
    })
    
    if image_url:
        # Add image as an image block for inline display
        blocks.append({
            "type": "image",
            "image_url": image_url,
            "alt_text": "Context screenshot"
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