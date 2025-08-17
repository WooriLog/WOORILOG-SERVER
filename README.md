과정 중심의 블로그를 지향하는, Woori.Log의 백엔드 레포지터리입니다.

## GIT 협업 규칙

### 커밋 전략

`git commit -m "[feat] 커밋 내용 작성"`
| 타입 | 의미 |
| ---------- | --------------------------- |
| `feat` | 새로운 기능 추가 (feature) |
| `fix` | 버그 수정 (bug fix) |
| `chore` | 코드 변경이 아닌 잡일, 설정 변경 등 |
| `build` | 의존성 추가, gradle 관련 변경 등 |
| `style` | 코드 포맷, 세미콜론, 공백 등 스타일 관련 수정 |
| `refactor` | 기능 변경 없이 코드 구조 개선 |
| `docs` | 문서 수정 |
| `test` | 테스트 코드 추가/수정 |
| `ci` | CI/CD 관련 설정 변경 |

### 브랜치 전략

- 기능 브랜치 전략 (github-flow)
<img width="807" height="196" alt="스크린샷 2025-08-18 00 16 53" src="https://github.com/user-attachments/assets/5321728b-070e-4e6d-81a5-dfb8b9452259" />

```
// {type}/#issue

- 🚀 feat/#1
- 🚨 fix/#5
- 🔧 refactor/#13
- 📃 docs/#17
```
