# Workflow de Git & GitHub CLI — Guia Operacional

Este guia define o passo a passo padronizado de desenvolvimento no LegacyDesk utilizando o Git e a GitHub CLI (`gh`). Ele complementa o `PROJECT_PROCESS.md`, servindo como referência rápida de comandos do início ao fim de uma Issue.

---

## 1. Regra Fundamental
- **Branch `main` protegida:** Nunca faça push direto para a `main`.
- Toda alteração deve nascer de uma **Issue**, passar por uma branch dedicada e ser integrada exclusivamente via **Pull Request (PR)**.

---

## 2. Nomenclatura Padrão de Branches

| Tipo | Prefixo | Exemplo |
| :--- | :--- | :--- |
| Nova funcionalidade | `feat/<issue>-<descricao>` | `feat/7-create-user-domain-entity` |
| Documentação | `docs/<issue>-<descricao>` | `docs/6-define-audit-trail-strategy` |
| Configuração / Setup | `chore/<issue>-<descricao>` | `chore/5-configure-flyway` |
| Testes | `test/<issue>-<descricao>` | `test/59-user-unit-tests` |
| Correção de Bug | `fix/<issue>-<descricao>` | `fix/35-invalid-status-transition` |
| Refatoração | `refactor/<issue>-<descricao>` | `refactor/2-isolate-domain-ports` |

---

## 3. Passo a Passo do Ciclo de Desenvolvimento

### Passo 1: Atualizar a branch principal localmente
Sempre inicie qualquer trabalho garantindo que a sua `main` local esteja em sincronia com o repositório remoto:

```powershell
git checkout main
git pull origin main
```

### Passo 2: Criar e vincular a branch à Issue automaticamente
Utilize a GitHub CLI para criar a branch já conectada ao rastreamento da Issue no GitHub:

```powershell
gh issue develop <NUMERO_DA_ISSUE> --checkout --name "<tipo>/<NUMERO_DA_ISSUE>-<descricao>"
```

*Exemplo para a Issue #7:*
```powershell
gh issue develop 7 --checkout --name "feat/7-create-user-domain-entity"
```

---

### Passo 3: Implementação, Validação e Testes
Durante o desenvolvimento:
- Mantenha alterações focadas exclusivamente no escopo da Issue.
- Execute os testes e certifique-se de que a aplicação compila sem erros:

```powershell
.\mvnw clean test
```

---

### Passo 4: Commit das Alterações
Siga estritamente o padrão do **Conventional Commits**:

```powershell
git add .
git commit -m "<tipo>: <descricao clara e concisa>"
```

*Exemplo:*
```powershell
git commit -m "feat: implement user domain model entity and role enum"
```

---

### Passo 5: Publicar a Branch no Repositório Remoto
Envie a branch local para o GitHub configurando o upstream:

```powershell
git push -u origin HEAD
```

*(O parâmetro `-u origin HEAD` publica a branch atual diretamente sem necessidade de digitar o nome completo da branch).*

---

### Passo 6: Criação do Pull Request (PR)

#### Opção A: Via Variável no PowerShell (Recomendado para seguir o template)
```powershell
$prBody = @"
## Summary
Breve resumo do que foi implementado ou alterado.

## Related Issue
Closes #<NUMERO_DA_ISSUE>

## Changes
- [x] Users / Employees
- [x] Security
- [x] Tests
- [x] Documentation

## How to Test
1. Executar `./mvnw test`
2. Validar que os testes da unidade de domínio foram executados com sucesso.

## Checklist
- [x] Project builds successfully
- [x] Tests pass
- [x] Validation was considered
- [x] No secrets committed
- [x] Documentation updated if necessary

## Security Considerations
- [x] Access control was considered
- [x] Sensitive data exposure was considered
- [x] Input validation was considered
"@

gh pr create --title "<tipo>: <descricao do pr>" --body $prBody --base main
```

#### Opção B: Criação Rápida (Preenchimento automático baseado no commit)
Se você já redigiu uma mensagem descritiva no último commit e deseja preencher rapidamente:
```powershell
gh pr create --fill --base main
```

---

### Passo 7: Mesclar e Limpar após Aprovação

Após o Pull Request ser revisado e aprovado:
1. Faça o merge via interface web do GitHub ou diretamente pelo terminal:
   ```powershell
   gh pr merge --merge --delete-branch
   ```
2. Retorne para a `main` e atualize:
   ```powershell
   git checkout main
   git pull origin main
   ```
