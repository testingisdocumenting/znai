# Znai Enterprise Sample Server - Local Testing Instructions

## Prerequisites

1. Python 3.8+
2. A Slack workspace where you can create apps
3. ngrok (for exposing local server to Slack)

## Setup

### 1. Create a Slack App

1. Go to https://api.slack.com/apps
2. Click "Create New App" > "From scratch"
3. Name your app (e.g., "Znai Bot") and select your workspace
4. Navigate to "OAuth & Permissions" in the sidebar
5. Under "Bot Token Scopes", add:
   - `chat:write`
   - `users:read`
6. Click "Install to Workspace" and authorize the app
7. Copy the "Bot User OAuth Token" (starts with `xoxb-`)

### 2. Install Dependencies

```bash
cd znai-enterprise-sample-server
pip install -r requirements.txt
```

### 3. Set Environment Variables

```bash
export SLACK_BOT_TOKEN=
export SLACK_CHANNEL="#your-channel-name"
```

### 4. Run the Server

```bash
python slack_bot.py
```

The server will start on http://localhost:5000

## Testing

### Using curl

```bash
curl -X POST http://localhost:5111/ask-in-slack \
  -H "Content-Type: application/json" \
  -d '{
    "username": "U096JGE7BPY",
    "message": "How do I configure the API endpoint?",
    "link": "https://example.com/docs/api-config"
  }'
```

### Testing with Code Snippets

```bash
curl -X POST http://localhost:5111/ask-in-slack \
  -H "Content-Type: application/json" \
  -d '{
    "username": "golubev.nikolay",
    "message": [
      {"type": "text", "content": "I have an issue with this code:"},
      {"type": "code", "content": "const api = new API();\napi.connect();", "language": "javascript"},
      {"type": "text", "content": "It throws an error on line 2."}
    ],
    "link": "https://example.com/docs/troubleshooting"
  }'
```

## Troubleshooting

1. **401 Unauthorized**: Check your SLACK_BOT_TOKEN
2. **Channel not found**: Ensure the bot is added to the channel
3. **CORS errors**: The Flask-CORS package should handle this, but check browser console
4. **User not tagged**: Ensure you're using the correct Slack user ID (starts with U)