@echo off
chcp 65001 >nul
echo ========================================================
echo   GERADOR DE ESTRUTURA E CODIGO COMPLETO - LEGACYDESK
echo ========================================================
echo.

if not exist export mkdir export

echo [1/2] Gerando arvore do projeto (estrutura_projeto.txt)...
tree /F /A > export\estrutura_projeto.txt

echo [2/2] Consolidando conteudo dos arquivos (projeto_completo.txt)...
(for /r %%f in (*.java *.md *.yml *.sql *.txt *.xml) do (
    @echo ========================================
    @echo ARQUIVO: %%f
    @echo ========================================
    @type "%%f"
    @echo.
    @echo.
)) > export\projeto_completo.txt

echo.
echo ========================================================
echo Sucesso! Arquivos salvos dentro da pasta 'export\':
echo  - export\estrutura_projeto.txt
echo  - export\projeto_completo.txt
echo ========================================================
echo.
pause
