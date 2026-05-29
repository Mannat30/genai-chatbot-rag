# 🤖 GenAI Chatbot with Spring Boot, Groq & RAG

## 📌 Overview

A GenAI-powered chatbot built using Java and Spring Boot that integrates with the Groq LLM API. The project demonstrates modern AI application concepts including REST APIs, conversation memory, prompt engineering, retrieval-augmented generation (RAG), and similarity-based context retrieval.

---

## 🚀 Features

* Spring Boot REST API
* Groq LLM Integration
* WebClient-based API Communication
* Conversation Memory
* Prompt Engineering
* Basic RAG Implementation
* Context Retrieval
* Similarity Search using Cosine Similarity
* Fake Embedding Generator for Learning Purposes
* JSON Parsing with Jackson ObjectMapper
* DTO-Based Architecture
* Environment Variable Support for API Keys

---

## 🏗️ Project Architecture

```text
Client (Postman)
        ↓
Controller Layer
        ↓
Service Layer
        ↓
Retriever
        ↓
Prompt Augmentation
        ↓
Groq API
        ↓
JSON Response
        ↓
ObjectMapper
        ↓
Client Response
```

---

## 🛠️ Tech Stack

* Java 21
* Spring Boot
* Maven
* Groq API
* WebClient
* Jackson ObjectMapper
* Lombok
* Git & GitHub

---

## 📂 Project Structure

```text
src/main/java
│
├── controller
│   └── ChatController
│
├── service
│   └── ChatService
│
├── dto
│   ├── ChatDto
│   └── ChatResponse
│
├── config
│   └── GroqConfig
│
└── embeddings
    ├── FakeEmbeddingGenerator
    └── SimilarityUtil
```

---

## 🔄 Request Flow

```text
User Question
↓
Controller
↓
ChatService
↓
Retrieve Relevant Context
↓
Build RAG Prompt
↓
Groq API
↓
AI Response
↓
Return Response
```

---

## 🧠 Concepts Implemented

### Spring Boot

* REST APIs
* Dependency Injection
* Service Layer
* DTO Pattern
* Configuration Management

### GenAI

* Prompt Engineering
* Conversation Memory
* Retrieval Augmented Generation (RAG)
* Similarity Search
* Context Retrieval

### AI Infrastructure

* Embeddings (Learning Version)
* Cosine Similarity
* Knowledge Chunking

---

## 📮 API Endpoint

### Chat Endpoint

```http
POST /chat
```

Request:

```json
{
  "message": "What is Java?"
}
```

Response:

```json
{
  "response": "Java is a platform-independent programming language..."
}
```

---

## ⚙️ Configuration

Set your Groq API Key as an environment variable:

```env
GROQ_API_KEY=your_api_key
```

application.yml:

```yaml
groq:
  api:
    key: ${GROQ_API_KEY}
```

---

## 🎯 Learning Outcomes

Through this project I learned:

* Spring Boot Architecture
* REST API Development
* External API Integration
* WebClient Usage
* JSON Parsing
* Prompt Engineering
* Conversation Memory
* Retrieval Augmented Generation (RAG)
* Similarity Search
* Embedding Concepts
* AI Application Development

---

## 🔮 Future Improvements

* Real Embedding Models
* Vector Database Integration (Pinecone/Qdrant/ChromaDB)
* PDF-Based RAG
* Semantic Search
* Spring AI Integration
* LangChain4j Integration
* Docker Deployment
* AWS Deployment

---

## 👨‍💻 Author

Developed as a learning project to understand GenAI application development using Java and Spring Boot.
