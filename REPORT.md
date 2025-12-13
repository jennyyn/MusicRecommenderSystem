# Project Report

## Challenges I Faced
**Challenge 1: Error Handling**
Problem: Error message was popping up in output that revealed private information about API.

Solution: Only displayed generic, user-friendly message in GUI 

Learned: Always separate developer-facing errors from user-facing messages

## Design Pattern Justifications
**Strategy Pattern:** Using the strategy pattern allowed for switching rewriting behavior at runtime without modifying the controller logic

**Factory Pattern:** This helped encapsulate object creation and keep the controller/service code simpler

**Observer Pattern:** Using an Observer-like approach made sure the GUI stays responsive and separate from API logic

## AI Usage 
Used ChatGPT to debug JSON parsing error

Asked: "Is my error handling robust?"

Modified: Used their suggestion to modify parts of my error handling
Verified: Tested bad input and bad wifi/api key

## Time Spent: ~16 hours