# Shared Preferences Plugin Structure

The Shared Preferences Plugin provides debugging capabilities for inspecting and manipulating Android's SharedPreferences. This document outlines the code structure of the Shared Preferences Plugin.

## Overview

The Shared Preferences Plugin is organized into the following structure:

```
com.pluto.plugins.preferences/
├── PlutoSharePreferencesPlugin.kt  # Main plugin class
├── BaseFragment.kt                 # Base fragment for UI
├── Session.kt                      # Session management
├── ui/                             # UI components
│   ├── DataModel.kt                # Data models
│   ├── SharedPrefViewModel.kt      # ViewModel
│   ├── ListFragment.kt             # List fragment
│   ├── SharedPrefAdapter.kt        # Adapter
│   └── KeyValueItemHolder.kt       # ViewHolder
└── utils/                          # Utilities
    ├── SharedPrefUtils.kt          # SharedPreferences utilities
    ├── EditProcessor.kt            # Edit functionality
    └── Preferences.kt              # Plugin preferences
```

## Core Components

### Main Plugin Classes

- **PlutoSharePreferencesPlugin**: The main entry point for the plugin that integrates with Pluto.
- **BaseFragment**: Base fragment for the plugin UI.
- **Session**: Manages the plugin session state.

### UI Components (`ui/`)

- **DataModel**: Data models for SharedPreferences.
- **SharedPrefViewModel**: ViewModel for the SharedPreferences UI.
- **ListFragment**: Fragment for displaying the list of preferences.
- **SharedPrefAdapter**: Adapter for displaying preferences.
- **KeyValueItemHolder**: ViewHolder for key-value items.

### Utilities (`utils/`)

- **SharedPrefUtils**: Utility functions for working with SharedPreferences.
- **EditProcessor**: Handles editing of preference values.
- **Preferences**: Manages plugin-specific preferences.

## Resources

The plugin includes various resources:

- **Layouts**: UI layouts for the plugin screens and items.
- **Drawables**: Icons and backgrounds used in the UI.
- **Menus**: Menu resources for actions.
- **Navigation**: Navigation graph for the plugin.
- **Strings**: String resources used in the UI.
- **Styles**: Style resources for UI components.

## Architecture

The Shared Preferences Plugin follows the MVVM (Model-View-ViewModel) architecture pattern:

1. **Model**: Data classes representing SharedPreferences.
2. **View**: UI components (Fragments, ViewHolders).
3. **ViewModel**: Manages UI-related data and business logic.

The plugin integrates with the main Pluto library through the Plugin interface defined in the Plugin Base module.

## Functionality

The Shared Preferences Plugin provides the following functionality:

1. **View Preferences**: Display all SharedPreferences in a structured format.
2. **Edit Preferences**: Modify preference values directly from the UI.
3. **Delete Preferences**: Remove preferences.
4. **Filter Preferences**: Filter preferences by key or value.
5. **Multiple Files**: Support for multiple SharedPreferences files.
6. **Type Support**: Support for various data types (String, Boolean, Integer, Float, Long, Set).

## Integration

The Shared Preferences Plugin automatically detects SharedPreferences in the application. No additional integration code is required beyond adding the plugin dependency to the project.

This structure allows developers to easily inspect and manipulate SharedPreferences during app development, providing valuable insights into the application's preference storage.

## Comparison with Datastore Plugin

The Shared Preferences Plugin is similar to the Datastore Plugin, but focuses specifically on Android's SharedPreferences API, while the Datastore Plugin targets Android's newer Datastore Preferences API. Both plugins provide similar functionality for their respective storage mechanisms, allowing developers to choose the appropriate tool based on their application's storage implementation.
