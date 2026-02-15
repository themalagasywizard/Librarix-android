# Librarix Android Conversion Checklist

**Last Updated:** February 15, 2026  
**Source:** iOS Librarix (~13,600 LOC, 60 files)

---

## Phase 1: Project Setup & Architecture
- [x] Create Android project structure
- [x] Configure Gradle with Kotlin DSL
- [x] Set up Jetpack Compose BOM
- [x] Configure dependency injection (Hilt)
- [x] Set up navigation (Compose Navigation)
- [ ] Configure build flavors (debug/release)
- [ ] Set up lint checks and unit tests baseline
- [x] Create MVVM + Clean Architecture structure
- [x] Set up Room database
- [ ] Set up Retrofit for Supabase

## Phase 2: Theme System
- [x] Map SwiftUI colors to Compose colors
- [x] Create Typography system matching iOS
- [x] Implement Light/Dark theme support
- [ ] Define Shape system (rounded corners)
- [x] Create Material3 theme wrapper

## Phase 3: Shared UI Components
- [x] CachedAsyncImage (coil) - uses Coil's AsyncImage
- [ ] ProfileAvatarView
- [x] BottomNavBar
- [x] ProgressRow (in CurrentlyReadingCard)
- [ ] Custom text fields and buttons
- [ ] Loading and error states

## Phase 4: Core Screens (P0)
- [x] HomeView (dashboard) - basic structure with CurrentlyReadingCard
- [ ] LibraryView (book list)
- [ ] BookDetailView (full details)
- [x] CurrentlyReadingCard (progress widget) - matches iOS UI
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
- [x] Initialize Android project structure
- [x] Set up Gradle, Hilt, Room, Compose
- [x] Implement theme colors from iOS Theme.swift
- [x] Create HomeScreen with CurrentlyReadingCard (matching iOS)
- [x] Set up bottom navigation
- [x] Create data layer (entities, DAOs, database)
- [x] Commit initial project setup
- [x] Set up cron job for progress tracking (every 2 hours)