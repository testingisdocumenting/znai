@echo off
SET CWD=%~dp0
java -cp "%CWD%\\lib\\*" org.testingisdocumenting.znai.cli.ZnaiCliApp $*
