@echo off
echo Pushing Java interview questions to GitHub...

git add MockInterviews/*.md
git commit -m "Add comprehensive Java interview questions collection"
git remote add origin https://github.com/kamkode/java-system-design-prep.git
git push -u origin main

echo Done! Check your GitHub repository.
pause
