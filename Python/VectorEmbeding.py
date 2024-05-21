# VectorEmbedding.py
from flask import Flask, request, jsonify
from transformers import AutoModel, AutoTokenizer
import torch
import faiss
import numpy as np

app = Flask(__name__)

# Load pre-trained model and tokenizer
model_name = "sentence-transformers/all-MiniLM-L6-v2"
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModel.from_pretrained(model_name)

# Assume we have some pre-indexed documents
# This is a placeholder; in a real application, you'd load or create this index from your document embeddings
documents = ["This is a document.", "Another document here.", "More documents are here."]
document_embeddings = []

for doc in documents:
    inputs = tokenizer(doc, return_tensors='pt', padding=True, truncation=True)
    with torch.no_grad():
        embedding = model(**inputs).last_hidden_state.mean(dim=1).numpy()
        document_embeddings.append(embedding[0])

document_embeddings = np.array(document_embeddings)
index = faiss.IndexFlatL2(document_embeddings.shape[1])
index.add(document_embeddings)

@app.route('/embed', methods=['POST'])
def embed():
    data = request.json

    print("Received data:", data)  # 요청 데이터를 출력하여 확인
    texts = data['texts']
    
    inputs = tokenizer(texts, return_tensors='pt', padding=True, truncation=True)
    with torch.no_grad():
        embeddings = model(**inputs).last_hidden_state.mean(dim=1).numpy()
    
    D, I = index.search(embeddings, k=5)  # Search top 5 closest documents
    results = []
    for idx_list in I:
        result_docs = [documents[i] for i in idx_list]
        results.append(result_docs)
    
    return jsonify({'results': results})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
