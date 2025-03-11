# Plugin Base Module Structure

The Plugin Base module provides the foundation for all Pluto plugins. This document outlines the code structure of the Plugin Base module.

## Overview

The Plugin Base module is organized into several key packages, each responsible for specific functionality:

```
com.pluto/
├── plugin/                   # Core plugin functionality
│   ├── libinterface/         # Interfaces for plugin integration
│   ├── settings/             # Plugin settings
│   └── share/                # Content sharing functionality
└── utilities/                # Utility classes and extensions
```

## Core Components

### Plugin Package (`plugin/`)
Contains core functionality for Pluto plugins:

- **Plugin.kt**: Base class for all plugins
- **PluginEntity.kt**: Data model for plugin information
- **PluginGroup.kt**: Grouping mechanism for plugins
- **DataModel.kt**: Data models for plugin functionality

#### Library Interface (`libinterface/`)
Interfaces for plugin integration with the main Pluto library:

- **FilesInterface**: Interface for file operations
- **LibraryInfoInterface**: Interface for library information
- **NotificationInterface**: Interface for notifications
- **PlutoInterface**: Main interface for plugin-Pluto communication

#### Settings (`settings/`)
Handles plugin settings:

- **SettingsPreferences**: Manages plugin settings storage and retrieval

#### Share (`share/`)
Functionality for sharing plugin content:

- **ContentShare**: Base functionality for sharing content
- **ShareFragment**: UI for sharing content
- **CSV Formatter**: Utility for formatting data as CSV

### Utilities Package (`utilities/`)
Contains utility classes and extensions used across plugins:

- **AutoClearedValue**: Lifecycle-aware value holder
- **DeBounceClickListener**: Prevents multiple rapid clicks
- **DebugLog**: Logging utility
- **SingleLiveEvent**: LiveData that emits once

#### Device (`device/`)
Device-related utilities:

- **Device**: Device information utilities
- **RootUtil**: Root detection utilities

#### Extensions (`extensions/`)
Kotlin extensions for various Android components:

- **AnimationKtx**: Animation extensions
- **ContextKtx**: Context extensions
- **DateKtx**: Date handling extensions
- **DimensKtx**: Dimension conversion extensions
- **KeyboardKtx**: Keyboard handling extensions
- **LayoutKtx**: Layout extensions
- **LifecycleKtx**: Lifecycle extensions
- **LiveDataKtx**: LiveData extensions
- **ViewKtx**: View extensions
- And more...

#### List (`list/`)
RecyclerView utilities:

- **BaseAdapter**: Base adapter for RecyclerView
- **DiffAwareAdapter**: Adapter with DiffUtil support
- **CustomItemDecorator**: Custom item decoration

#### Selector (`selector/`)
Data selection utilities:

- **DataSelector**: Utility for data selection
- **UI components**: Adapters and dialogs for selection

#### Views (`views/`)
Custom views:

- **TabularDataView**: View for displaying tabular data
- **KeyValuePairView**: View for key-value pairs
- **Editor components**: UI for editing key-value pairs

## Architecture

The Plugin Base module follows a modular architecture that provides:

1. **Base Classes**: Abstract classes and interfaces that plugins must implement
2. **Utility Functions**: Common functionality used across plugins
3. **UI Components**: Reusable UI elements for plugin interfaces

This architecture allows for consistent plugin development while providing flexibility for plugin-specific functionality.
