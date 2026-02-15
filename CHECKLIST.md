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
- [x] LibraryView (book list) - search, filters, grid/list views
- [x] BookDetailView (full details) - hero, synopsis, actions bar
- [x] CurrentlyReadingCard (progress widget) - matches iOS UI
- [x] CurrentlyReadingListView (list) - progress list matching iOS
- [x] UpdateProgressView (modal) - slider, page input, notes

## Phase 5: Discovery Screens (P1)
- [x] DiscoverView (browse/search) - search, curated collections
- [ ] DiscoverBookDetailView
- [ ] AddBookView
- [ ] ISBNScannerView
- [ ] Search functionality

## Phase 6: Reading & Progress (P2)
- [x] ProgressDashboardView - XP levels, stats, achievements
- [x] Reading statistics
- [x] Yearly goals
- [x] Progress history

## Phase 7: Collections & Notes (P3)
- [x] MyCollectionsView - create/view collections
- [x] UserCollectionDetailView - hero header, book grid
- [x] AllNotesLibraryView - notes list view
- [x] SettingsView

## Phase 8: Profile & Onboarding (P4)
- [x] OnboardingFlowView - welcome, profile setup, completion
- [x] EditProfileView - name, avatar icon/color
- [x] ProfileView - stats, achievements, reading lists
- [ ] AuthorDetailView

## Phase 9: Backend Integration
- [x] Open Library API - search and book details
- [x] Amazon Affiliate links - buy link generation
- [ ] Supabase Auth
- [ ] Supabase Database sync

## Phase 10: Testing & Polish
- [ ] Unit tests
- [ ] UI tests
- [ ] Integration tests
- [ ] Performance optimization
- [ ] Accessibility
- [ ] Animations

---

## Daily Progress Log

### 2026-02-15 (Session 2)
- [x] Add BookDetailScreen, CurrentlyReadingListScreen, DiscoverScreen
- [x] Add ProgressDashboardScreen with XP levels
- [x] Add SettingsScreen and ProfileScreen
- [x] Add OnboardingFlowScreen
- [x] Add EditProfileScreen
- [x] Add MyCollectionsScreen
- [x] Add UserCollectionDetailScreen
- [x] Add AllNotesLibraryScreen
- [x] Add OpenLibraryClient for API
- [x] Add NetworkModule for Retrofit
- [x] Add AmazonAffiliateLinkGenerator

### 2026-02-15 (Session 1)
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
- [x] Set up cron job for progress tracking (every hour)