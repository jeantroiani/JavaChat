# Simple Chatting System

## Description:
This is a chat application done in Java using a combination of threads and socket to allow communication between multiple clients at the same time.

## How to start it:

### Server
Note: The server should be running first otherwise clients would not be able to connect and will return an error.

- Run in the CLI
```sh
java ChatServer
```
Optionals:
-csp: 
Changes the default PORT number to any number between 1024 and 49151.
```sh
java ChatServer -csp 3000
```
### Client
Note: Flags can be combined but each flag must have a key and value present to be evaluated.

- Run in the CLI
```sh
java ChatClient
```
Optionals:
-ccp: 
Changes the default PORT number to any number between 1024 and 49151.
```sh
java ChatClient -ccp 3000
```
-cca: 
Changes the default PORT number to any number.
```sh
java ChatClient -cca 127.0.0.1
```
-name: 
Changes the name of the client
```sh
java ChatClient -name Edward
```

### Bot
- Run in the CLI
```sh
java ChatBot
```