@echo off
echo Pushing all Java interview project files to GitHub...

git add .
git commit -m "Add comprehensive Java interview questions and project files"
git remote add origin https://github.com/kamkode/java-system-design-prep.git
git push -u origin main

echo Done! Check your GitHub repository.
pause
