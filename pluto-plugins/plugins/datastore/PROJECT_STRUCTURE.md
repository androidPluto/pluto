# Datastore Plugin Structure

The Datastore Plugin provides debugging capabilities for Android's Datastore Preferences. This document outlines the code structure of the Datastore Plugin.

## Overview

The Datastore Plugin is organized into the following structure:

```
com.pluto.plugins.datastore/
└── pref/                     # Datastore Preferences functionality
    ├── PlutoDatastorePreferencesPlugin.kt  # Main plugin class
    ├── PlutoDatastoreWatcher.kt            # Watcher for datastore changes
    ├── Session.kt                          # Session management
    └── internal/                           # Internal implementation
        ├── ui/                             # UI components
        ├── DatastorePrefUtils.kt           # Utility functions
        ├── EditProcessor.kt                # Edit functionality
        └── Preferences.kt                  # Preferences management
```

## Core Components

### Main Plugin Class

- **PlutoDatastorePreferencesPlugin**: The main entry point for the plugin that integrates with Pluto.

### Datastore Preferences Package (`pref/`)

- **PlutoDatastoreWatcher**: Monitors changes to Datastore Preferences.
- **Session**: Manages the plugin session state.
- **BaseFragment**: Base fragment for the plugin UI.

### Internal Implementation (`internal/`)

- **DatastorePrefUtils**: Utility functions for working with Datastore Preferences.
- **EditProcessor**: Handles editing of preference values.
- **Preferences**: Manages plugin-specific preferences.

#### UI Components (`internal/ui/`)

- **DatastorePrefAdapter**: Adapter for displaying preferences.
- **DatastorePrefViewModel**: ViewModel for the preferences UI.
- **KeyValueItemHolder**: ViewHolder for key-value items.
- **ListFragment**: Fragment for displaying the list of preferences.

## Resources

The plugin includes various resources:

- **Layouts**: UI layouts for the plugin screens and items.
- **Drawables**: Icons and backgrounds used in the UI.
- **Navigation**: Navigation graph for the plugin.
- **Strings**: String resources used in the UI.

## Architecture

The Datastore Plugin follows the MVVM (Model-View-ViewModel) architecture pattern:

1. **Model**: Data classes representing Datastore Preferences.
2. **View**: UI components (Fragments, ViewHolders).
3. **ViewModel**: Manages UI-related data and business logic.

The plugin integrates with the main Pluto library through the Plugin interface defined in the Plugin Base module.

## Functionality

The Datastore Plugin provides the following functionality:

1. **View Preferences**: Display all Datastore Preferences in a structured format.
2. **Edit Preferences**: Modify preference values directly from the UI.
3. **Monitor Changes**: Track changes to preferences in real-time.
4. **Search and Filter**: Find specific preferences quickly.

This structure allows developers to easily debug and manipulate Datastore Preferences during app development.
