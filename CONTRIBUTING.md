# Contributing to Skeleton Viewer

Thank you for your interest in contributing!

## Getting Started

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Make your changes following the code style below
4. Write or update tests
5. Commit with a clear message: `git commit -m "feat: add gesture recognition"`
6. Push and open a Pull Request

## Code Style

- **Kotlin** only — no Java files
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use Jetpack Compose for all UI
- Follow MVVM + Clean Architecture layering:
  - `data` layer does NOT import from `presentation`
  - `domain` layer does NOT import from `data` or `presentation`
  - `presentation` imports from `domain` only
- Inject dependencies via Hilt — no manual instantiation
- Use `StateFlow` for UI state, not `LiveData`

## Commit Message Format

```
type(scope): short description

feat(camera): add zoom gesture support
fix(detector): handle null landmarks gracefully
refactor(overlay): extract head drawing to separate method
test(pose): add landmark index validation
docs(readme): update build instructions
```

Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `perf`

## Pull Request Guidelines

- Link related issues
- Include screenshots or screen recordings for UI changes
- Ensure all existing tests pass: `./gradlew test`
- Add tests for new functionality
- Update CHANGELOG.md under `[Unreleased]`

## Reporting Bugs

Open a GitHub Issue with:
- Android version and device model
- Steps to reproduce
- Expected vs actual behavior
- Logcat output (filtered by `SkeletonViewer`)

## Feature Requests

Open a GitHub Issue with:
- Clear description of the feature
- Use case / problem it solves
- Any reference implementations or screenshots

## License

By contributing, you agree that your contributions will be licensed under the MIT License.
