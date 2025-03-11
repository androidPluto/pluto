# Logger Plugin Structure

The Logger Plugin provides debugging capabilities for logging and analyzing application logs. This document outlines the code structure of the Logger Plugin.

## Overview

The Logger Plugin is organized into the following structure:

```
com.pluto.plugins.logger/
├── PlutoLoggerPlugin.kt       # Main plugin class
├── PlutoLog.kt                # Public logging API
├── PlutoTimberTree.kt         # Timber integration
├── FilterViewModel.kt         # ViewModel for log filtering
├── Preferences.kt             # Plugin preferences
└── internal/                  # Internal implementation
    ├── DataModel.kt           # Data models
    ├── LogsFragment.kt        # Main logs fragment
    ├── LogsProcessor.kt       # Log processing
    ├── LogsViewModel.kt       # ViewModel for logs
    ├── Session.kt             # Session management
    ├── ThrowableKtx.kt        # Throwable extensions
    ├── persistence/           # Data persistence
    └── ui/                    # UI components
```

## Core Components

### Main Plugin Classes

- **PlutoLoggerPlugin**: The main entry point for the plugin that integrates with Pluto.
- **PlutoLog**: Public API for logging messages.
- **PlutoTimberTree**: Integration with the Timber logging library.
- **FilterViewModel**: ViewModel for filtering logs.
- **Preferences**: Manages plugin-specific preferences.

### Internal Implementation (`internal/`)

- **DataModel**: Data models for logs.
- **LogsFragment**: Main fragment for displaying logs.
- **LogsProcessor**: Processes and formats logs.
- **LogsViewModel**: ViewModel for logs UI.
- **Session**: Manages the plugin session state.
- **ThrowableKtx**: Extensions for handling throwables.

#### Persistence (`internal/persistence/`)

- **LogDao**: Data access object for logs.
- **LogDBHandler**: Database handler for logs.
- **LogEntity**: Entity class for logs.
- **EntityConverters**: Type converters for database.
- **Database**: Room database implementation.
  - **DatabaseManager**: Manages database instances.
  - **PlutoDatabase**: Room database definition.

#### UI Components (`internal/ui/`)

- **DetailsFragment**: Fragment for log details.
- **ListFragment**: Fragment for displaying the list of logs.
- **List Components**: UI for displaying logs.
  - **LogsAdapter**: Adapter for displaying logs.
  - **LogItemHolder**: ViewHolder for log items.
  - **LogHeaderHolder**: ViewHolder for log headers.

## Resources

The plugin includes various resources:

- **Layouts**: UI layouts for the plugin screens and items.
- **Drawables**: Icons and backgrounds used in the UI.
- **Menus**: Menu resources for actions.
- **Navigation**: Navigation graph for the plugin.
- **Colors**: Color resources.
- **Strings**: String resources used in the UI.
- **Styles**: Style resources for UI components.

## Architecture

The Logger Plugin follows the MVVM (Model-View-ViewModel) architecture pattern:

1. **Model**: Data classes representing logs.
2. **View**: UI components (Fragments, ViewHolders).
3. **ViewModel**: Manages UI-related data and business logic.

The plugin integrates with the main Pluto library through the Plugin interface defined in the Plugin Base module.

## Functionality

The Logger Plugin provides the following functionality:

1. **Logging**: Capture and display application logs.
2. **Filtering**: Filter logs by level, tag, or content.
3. **Log Details**: View detailed information about each log.
4. **Timber Integration**: Capture logs from Timber.
5. **Persistence**: Store logs for later analysis.
6. **Sharing**: Share log details for debugging.
7. **Clear Logs**: Clear all logs or filtered logs.

This structure allows developers to easily track and debug application logs during development.

## Usage

The Logger Plugin can be used in two main ways:

1. **Direct API**: Use `PlutoLog` to log messages directly.
   ```kotlin
   PlutoLog.d("TAG", "Debug message")
   PlutoLog.e("TAG", "Error message", exception)
   ```

2. **Timber Integration**: Use the `PlutoTimberTree` with Timber.
   ```kotlin
   Timber.plant(PlutoTimberTree())
   Timber.d("Debug message")
   ```

The logs are displayed in the Pluto UI and can be filtered, searched, and shared for debugging purposes.
