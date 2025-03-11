# Exceptions Plugin Structure

The Exceptions Plugin provides debugging capabilities for tracking and analyzing application exceptions and ANRs (Application Not Responding). This document outlines the code structure of the Exceptions Plugin.

## Overview

The Exceptions Plugin is organized into the following structure:

```
com.pluto.plugins.exceptions/
├── PlutoExceptionsPlugin.kt      # Main plugin class
├── PlutoExceptions.kt            # Public API for the plugin
├── ANRException.kt               # ANR exception model
├── UncaughtANRHandler.kt         # Handler for ANRs
└── internal/                     # Internal implementation
    ├── anr/                      # ANR detection
    ├── crash/                    # Crash handling
    ├── extensions/               # Utility extensions
    ├── persistence/              # Data storage
    ├── ui/                       # UI components
    ├── BaseFragment.kt           # Base fragment for UI
    ├── DataModel.kt              # Data models
    └── Session.kt                # Session management
```

## Core Components

### Main Plugin Classes

- **PlutoExceptionsPlugin**: The main entry point for the plugin that integrates with Pluto.
- **PlutoExceptions**: Public API for interacting with the plugin.
- **ANRException**: Custom exception class for ANRs.
- **UncaughtANRHandler**: Handler for uncaught ANRs.

### Internal Implementation (`internal/`)

- **BaseFragment**: Base fragment for the plugin UI.
- **DataModel**: Data models for exceptions.
- **Session**: Manages the plugin session state.

#### ANR Detection (`internal/anr/`)

- **AnrSupervisor**: Monitors the application for ANRs.
- **AnrSupervisorCallback**: Callback interface for ANR detection.
- **AnrSupervisorRunnable**: Runnable for ANR detection.

#### Crash Handling (`internal/crash/`)

- **CrashHandler**: Handles application crashes.
- **CrashNotification**: Notification for crashes.

#### Extensions (`internal/extensions/`)

- **ConcurrencyKtx**: Extensions for concurrency.
- **ThreadKtx**: Extensions for thread operations.

#### Persistence (`internal/persistence/`)

- **ExceptionDao**: Data access object for exceptions.
- **ExceptionDBHandler**: Database handler for exceptions.
- **ExceptionEntity**: Entity class for exceptions.
- **Preferences**: Manages plugin-specific preferences.
- **Database**: Room database implementation.

#### UI Components (`internal/ui/`)

- **CrashesAdapter**: Adapter for displaying crashes.
- **CrashesViewModel**: ViewModel for the crashes UI.
- **DetailsFragment**: Fragment for exception details.
- **ListFragment**: Fragment for displaying the list of exceptions.
- **StackTracesAdapter**: Adapter for displaying stack traces.
- **ThreadStackTraceFragment**: Fragment for thread stack traces.
- **Various holders**: ViewHolders for different UI components.

## Resources

The plugin includes various resources:

- **Layouts**: UI layouts for the plugin screens and items.
- **Drawables**: Icons and backgrounds used in the UI.
- **Menus**: Menu resources for actions.
- **Navigation**: Navigation graph for the plugin.
- **Strings**: String resources used in the UI.

## Architecture

The Exceptions Plugin follows the MVVM (Model-View-ViewModel) architecture pattern:

1. **Model**: Data classes representing exceptions and ANRs.
2. **View**: UI components (Fragments, ViewHolders).
3. **ViewModel**: Manages UI-related data and business logic.

The plugin integrates with the main Pluto library through the Plugin interface defined in the Plugin Base module.

## Functionality

The Exceptions Plugin provides the following functionality:

1. **Exception Tracking**: Capture and display uncaught exceptions.
2. **ANR Detection**: Detect and report Application Not Responding situations.
3. **Stack Trace Analysis**: View detailed stack traces for exceptions.
4. **Thread Information**: View thread state at the time of exception.
5. **Device Information**: View device details for context.
6. **Persistence**: Store exceptions for later analysis.
7. **Sharing**: Share exception details for debugging.

This structure allows developers to easily track and debug exceptions during app development.
