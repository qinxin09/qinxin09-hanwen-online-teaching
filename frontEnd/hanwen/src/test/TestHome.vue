<template>
    <div>
        <input type="file" @change="handleFileChange" />
        <button @click="uploadFile">上传文件</button>
        <div v-if="progress.chunk > 0">当前片上传进度：{{ progress.chunk }}%</div>
        <div v-if="progress.all > 0">上传总进度：{{ progress.all }}%</div>
        <div v-if="message !== ''">上传结果：{{ message }}</div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name:"TestHome",
    data() {
      return {
        file: null,
        progress:{
            chunk:0,
            all:0
        },
        message: ''
      };
    },
    methods: {
      handleFileChange(event) {
        this.file = event.target.files[0];
      },

      async uploadFile() {
        const CHUNK_SIZE = 1024 * 1024 ; // 1MB
        const TOTAL_CHUNKS = Math.ceil(this.file.size / CHUNK_SIZE);
        
        let currentChunk = 0;
  
        while (currentChunk < TOTAL_CHUNKS) {
          const start = currentChunk * CHUNK_SIZE;
          const end = Math.min(start + CHUNK_SIZE, this.file.size);
          
          const chunk = this.file.slice(start, end);
  
          const formData = new FormData();
          formData.append('file', chunk);
          formData.append('filename', this.file.name);
          formData.append('chunkIndex', currentChunk);
          formData.append('totalChunks', TOTAL_CHUNKS);
  
          await axios.post('http://localhost:8300/chunk-upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
            onUploadProgress: (progressEvent) => {
              const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
              this.progress.chunk = progress;
              this.progress.all = Math.round(((start+progressEvent.loaded) * 100) / this.file.size);
              console.log(start,progressEvent.loaded, this.file.size);

            }
          }).then(response => {
            this.message = response.data.message;
          }).catch(error => {
            this.message = '上传失败';
          });
  
          currentChunk++;
        }
        
      }
    }
  }
  </script>