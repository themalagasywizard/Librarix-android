# Librarix iOS to Android Conversion Plan

**Created:** February 15, 2026  
**Source Repo:** `themalagasywizard/Librarix` (iOS)  
**Target Repo:** `themalagasywizard/librarix-android`

---

## Executive Summary

Convert Librarix (a book tracking and discovery iOS app) to Android with Jetpack Compose, maintaining identical UI/UX and feature parity.

**Source App Metrics:**
- **Language:** SwiftUI (iOS 17+)
- **Files:** 60 Swift files, ~13,600 LOC
- **Architecture:** MVVM + SwiftData
- **Backend:** Supabase (PostgreSQL + Auth)
- **External APIs:** Open Library, Amazon Affiliate

---

## Phase 1: Project Setup & Architecture (Week 1)

### 1.1 Android Project Initialization
- [ ] Create Android project structure
- [ ] Configure Gradle with Kotlin DSL
- [ ] Set up Jetpack Compose BOM
- [ ] Configure dependency injection (Hilt)
- [ ] Set up navigation (Compose Navigation)
- [ ] Configure build flavors (debug/release)
- [ ] Set up lint checks and unit tests baseline

### 1.2 Architecture Definition
- **Pattern:** MVVM + Clean Architecture
- **UI Layer:** Jetpack Compose
- **Data Layer:** Room (local) + Retrofit (Supabase)
- **State Management:** ViewModel + StateFlow
- **DI:** Hilt

### 1.3 Theme System (Critical for Look & Feel)
- [ ] Map SwiftUI colors to Compose colors
- [ ] Create Typography system matching iOS
- [ ] Implement Light/Dark theme support
- [ ] Define Shape system (rounded corners)
- [ ] Create Material3 theme wrapper

### 1.4 Shared Theme Constants
```kotlin
// Color mapping from SwiftUI
val LxPrimary = Color(0xFF1C4E5E)  // #1c4e5e
val LxPrimaryLight = Color(0xFF2A6F85)
val LxAccentGold = Color(0xFFD1A000)
val LxBackgroundLight = Color(0xFFF9FAFB)
val LxBackgroundDark = Color(0xFF131C20)
val LxSurfaceDark = Color(0xFF1E2B34)
val LxSurfaceLight = Color.White
val LxTextSecondary = Color(0xFF778A9B)
```

---

## Phase 2: Core Infrastructure (Week 1-2)

### 2.1 Data Layer
- [ ] Create Room database entities (SavedBook, UserCollection, BookNote)
- [ ] Create DAOs for each entity
- [ ] Implement Repository pattern
- [ ] Set up data mapping between entities and domain models

### 2.2 Network Layer
- [ ] Configure Retrofit for Supabase
- [ ] Implement Auth API (Supabase Auth)
- [ ] Create API client for Open Library
- [ ] Implement Amazon Affiliate links
- [ ] Set up OkHttp interceptors

### 2.3 Authentication
- [ ] Supabase Auth integration
- [ ] Email/password login
- [ ] Social auth (Google - since Apple Sign In unavailable)
- [ ] Session management
- [ ] Secure storage (EncryptedSharedPreferences)

### 2.4 Navigation
- [ ] Set up Compose Navigation graph
- [ ] Define navigation arguments
- [ ] Implement bottom navigation (matching iOS tabs)
- [ ] Deep link handling

---

## Phase 3: UI Implementation (Week 2-4)

### 3.1 Shared UI Components
- [ ] `CachedAsyncImage` (replaces iOS CachedAsyncImage)
- [ ] `ProfileAvatarView`
- [ ] `BottomNavBar`
- [ ] `ProgressRow`
- [ ] Custom text fields and buttons
- [ ] Loading and error states

### 3.2 Screen Implementation Priority

#### P0 - Core Screens
- [ ] **HomeView** (~911 LOC) - Dashboard with currently reading, goals
- [ ] **LibraryView** (~424 LOC) - Main book library list
- [ ] **BookDetailView** (~1161 LOC) - Full book details with notes
- [ ] **CurrentlyReadingCard** - Progress tracking widget

#### P1 - Discovery & Add
- [ ] **DiscoverView** (~1125 LOC) - Browse and search books
- [ ] **DiscoverBookDetailView** (~562 LOC) - Book discovery details
- [ ] **AddBookView** (~779 LOC) - Add books to library
- [ ] **ISBNScannerView** (~218 LOC) - Barcode scanning

#### P2 - Reading & Progress
- [ ] **CurrentlyReadingListView** (~204 LOC) - List view
- [ ] **UpdateProgressView** (~245 LOC) - Progress update modal
- [ ] **ProgressDashboardView** (~800 LOC) - Reading statistics

#### P3 - Collections & Notes
- [ ] **MyCollectionsView** (~187 LOC) - User collections
- [ ] **UserCollectionDetailView** (~333 LOC) - Collection details
- [ ] **AllNotesLibraryView** (~65 LOC) - Notes overview
- [ ] **SettingsView** (~554 LOC) - App settings

#### P4 - Profile & Onboarding
- [ ] **OnboardingFlowView** (~258 LOC) - First-time user flow
- [ ] **EditProfileView** (~209 LOC) - Profile editing
- [ ] **AuthorDetailView** (~290 LOC) - Author profiles

### 3.3 UI Implementation Details

#### Home Screen Components
```kotlin
@Composable
fun HomeView(
    viewModel: HomeViewModel = hiltViewModel()
) {
    // CurrentlyReadingCard with UpdateProgress button
    // GoalCard (yearly reading goal)
    // FreshlyShelved section
    // LatestNotes section
}
```

#### Currently Reading Logic
- **Home Card:** Tap cover → BookDetailView; Tap "Update Progress" → UpdateProgressView
- **View All List:** Tap book → UpdateProgressView (per recent requirement)

#### Book Detail Screen
- Hero cover image with shadow
- Title, author, metadata
- Synopsis expandable section
- Progress tracking
- Notes management
- Amazon buy links

---

## Phase 4: Feature Parity (Week 4-5)

### 4.1 Book Management
- [ ] Add books from Open Library search
- [ ] Manual ISBN entry
- [ ] Barcode scanning (ML Kit)
- [ ] Edit book details
- [ ] Delete books

### 4.2 Progress Tracking
- [ ] Update reading progress (pages)
- [ ] Progress history/logging
- [ ] Reading streaks
- [ ] Yearly reading goals

### 4.3 Collections
- [ ] Create custom collections
- [ ] Add/remove books from collections
- [ ] Curated collections display

### 4.4 Notes System
- [ ] Add notes to books
- [ ] View all notes
- [ ] Edit/delete notes
- [ ] Note search

### 4.5 External Integrations
- [ ] Open Library API integration
- [ ] Amazon Affiliate link generation
- [ ] Supabase sync (cloud backup)

---

## Phase 5: Testing & Polish (Week 5-6)

### 5.1 Testing
- [ ] Unit tests for ViewModels
- [ ] Repository tests
- [ ] UI tests (Compose)
- [ ] Integration tests

### 5.2 Performance
- [ ] Image caching optimization
- [ ] Lazy loading lists
- [ ] Memory optimization
- [ ] Build time optimization

### 5.3 Polish
- [ ] Animations matching iOS
- [ ] Gestures implementation
- [ ] Accessibility (TalkBack)
- [ ] RTL support

---

## Technical Decisions

### iOS → Android Mapping

| iOS (SwiftUI) | Android (Compose) |
|---------------|-------------------|
| @State | var/remember |
| @StateObject | viewModel() |
| @EnvironmentObject | hiltViewModel() + DI |
| NavigationView | NavHost |
| .sheet() | ModalBottomSheet |
| .fullScreenCover() | Dialog/Dialog |
| @Query (SwiftData) | Room DAO |
| @Published | MutableStateFlow |

### Libraries Required
```kotlin
// Core
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.compose)
implementation(libs.androidx.navigation.compose)
implementation(libs.hilt.android)
implementation(libs.hilt.navigation.compose)
kapt(libs.hilt.compiler)

// Data
implementation(libs.room.runtime)
implementation(libs.room.ktx)
implementation(libs.retrofit)
implementation(libs.okhttp)
implementation(libs.moshi)

// Image
implementation(libs.coil.compose)
implementation(libs.camerax)

// Auth
implementation(libs.supabase.gotrue)
implementation(libs.supabase.postgrest)
```

---

## File Structure

```
librarix-android/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/librarix/
│       │   │   ├── data/
│       │   │   │   ├── local/ (Room)
│       │   │   │   ├── remote/ (Retrofit)
│       │   │   │   ├── repository/
│       │   │   │   └── di/
│       │   │   ├── domain/
│       │   │   │   ├── model/
│       │   │   │   ├── repository/
│       │   │   │   └── usecase/
│       │   │   ├── presentation/
│       │   │   │   ├── ui/
│       │   │   │   │   ├── components/
│       │   │   │   │   ├── screens/
│       │   │   │   │   └── theme/
│       │   │   │   └── viewmodel/
│       │   │   └── LibrarixApp.kt
│       │   └── res/
│       └── test/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## Progress Tracking

**Last Updated:** February 15, 2026  
**Status:** Planning Phase Complete

### Completion Checklist
- [x] Source code analysis
- [x] Conversion plan created
- [x] Android project initialized (pending)
- [ ] Theme system implemented
- [ ] Core infrastructure complete
- [ ] All screens implemented
- [ ] Feature parity achieved
- [ ] Testing complete
- [ ] Production ready

---

## Next Steps

1. Initialize Android project with proper Gradle setup
2. Create theme system matching iOS colors
3. Set up Room database with SavedBook entity
4. Implement Home screen (highest priority)
5. Iterate screen by screen