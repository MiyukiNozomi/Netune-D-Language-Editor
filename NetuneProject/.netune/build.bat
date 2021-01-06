@echo off
cd C:\Users\stilk\Documents\NetuneProject
title Running
dub build
echo --------------------
cls
netuneproject.exe
echo --------------------
echo [Netune] CMD Window closing in 45 seconds
ping localhost -n 45 > nul
exit