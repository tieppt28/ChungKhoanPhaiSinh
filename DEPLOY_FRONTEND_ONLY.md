# 🚀 DEPLOY CHỈ FRONTEND

## 🎯 **GIẢI PHÁP ĐƠN GIẢN NHẤT**

### **Option 1: Deploy từ thư mục frontend**

1. **Tạo repo mới chỉ cho frontend**
   ```bash
   # Tạo thư mục mới
   mkdir chung-khoan-frontend
   cd chung-khoan-frontend
   
   # Copy frontend code
   cp -r ../ChungKhoanPhaiSinh/frontend/* .
   
   # Init git
   git init
   git add .
   git commit -m "Initial commit"
   
   # Push lên GitHub
   git remote add origin <your-new-repo-url>
   git push -u origin main
   ```

2. **Deploy lên Vercel**
   - Connect repo mới
   - Vercel sẽ tự động detect React app
   - Deploy thành công

### **Option 2: Cấu hình Vercel cho monorepo**

1. **Trong Vercel Dashboard:**
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `build`

2. **Hoặc tạo file `.vercelignore`**
   ```
   src/
   target/
   pom.xml
   *.md
   !frontend/
   ```

### **Option 3: Sử dụng Netlify**

1. **Deploy trực tiếp**
   ```bash
   cd frontend
   npm run build
   # Upload thư mục build lên Netlify
   ```

2. **Hoặc connect GitHub**
   - **Base directory**: `frontend`
   - **Build command**: `npm run build`
   - **Publish directory**: `build`

## 🎯 **KHUYẾN NGHỊ**

**Deploy từ thư mục frontend riêng biệt** - Đơn giản nhất và ít lỗi nhất!
