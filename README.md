# Chat Assistant
## Setup
1. Clone the repository: `https://github.com/jennyyn/ChatAssistant.git`
2. Get API key from [OpenAI Website](https://platform.openai.com/docs/overview)
3. `export API_KEY="your=key"`
5. Run `Main.java`

## Features 
- Creative writing mode
- Professional writing mode
- Academic writing mode
- Save/Load sessions
    - allows you to save your current session or load past sessions
- Cancel request (NEW)
    - allows you to cancel your request to rewrite your text
- Delete sessions (NEW)
    - allows you to select a specific session and delete it

## Design Patterns
- Strategy: Different writing modes (creative, professional, academic)
- Factory: Request creation
- Observer: UI updates
- Singleton: APIClient / Config

## Demos: 
Final: https://youtu.be/oTE-EoeeHTY

Assignment #3: https://youtu.be/4iZehUSWglI?si=71g1zYqXqEQC3HbC
