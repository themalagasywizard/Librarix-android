# Librarix Android Conversion Checklist

**Last Updated:** February 15, 2026  
**Source:** iOS Librarix (~13,600 LOC, 60 files)

---

## Phase 1: Project Setup & Architecture
- [ ] Create Android project structure
- [ ] Configure Gradle with Kotlin DSL
- [ ] Set up Jetpack Compose BOM
- [ ] Configure dependency injection (Hilt)
- [ ] Set up navigation (Compose Navigation)
- [ ] Configure build flavors (debug/release)
- [ ] Set up lint checks and unit tests baseline
- [ ] Create MVVM + Clean Architecture structure
- [ ] Set up Room database
- [ ] Set up Retrofit for Supabase

## Phase 2: Theme System
- [ ] Map SwiftUI colors to Compose colors
- [ ] Create Typography system matching iOS
- [ ] Implement Light/Dark theme support
- [ ] Define Shape system (rounded corners)
- [ ] Create Material3 theme wrapper

## Phase 3: Shared UI Components
- [ ] CachedAsyncImage (coil)
- [ ] ProfileAvatarView
- [ ] BottomNavBar
- [ ] ProgressRow
- [ ] Custom text fields and buttons
- [ ] Loading and error states

## Phase 4: Core Screens (P0)
- [ ] HomeView (dashboard)
- [ ] LibraryView (book list)
- [ ] BookDetailView (full details)
- [ ] CurrentlyReadingCard (progress widget)
- [ ] CurrentlyReadingListView (list)
- [ ] UpdateProgressView (modal)

## Phase 5: Discovery Screens (P1)
- [ ] DiscoverView (browse/search)
- [ ] DiscoverBookDetailView
- [ ] AddBookView
- [ ] ISBNScannerView
- [ ] Search functionality

## Phase 6: Reading & Progress (P2)
- [ ] ProgressDashboardView
- [ ] Reading statistics
- [ ] Yearly goals
- [ ] Progress history

## Phase 7: Collections & Notes (P3)
- [ ] MyCollectionsView
- [ ] UserCollectionDetailView
- [ ] AllNotesLibraryView
- [ ] SettingsView

## Phase 8: Profile & Onboarding (P4)
- [ ] OnboardingFlowView
- [ ] EditProfileView
- [ ] AuthorDetailView

## Phase 9: Backend Integration
- [ ] Supabase Auth
- [ ] Supabase Database sync
- [ ] Open Library API
- [ ] Amazon Affiliate links

## Phase 10: Testing & Polish
- [ ] Unit tests
- [ ] UI tests
- [ ] Integration tests
- [ ] Performance optimization
- [ ] Accessibility
- [ ] Animations

---

## Daily Progress Log

### 2026-02-15
- [x] Analyzed iOS codebase (60 files, ~13,600 LOC)
- [x] Created conversion plan (ANDROID_CONVERSION_PLAN.md)
- [x] Created checklist (this file)
- [ ] Initialize Android project structure