# Pluto Module Structure

Pluto is an on-device debugger for Android applications. This document outlines the code structure of the main Pluto module.

## Overview

The Pluto module is organized into several key packages, each responsible for specific functionality:

```
com.pluto/
├── Pluto.kt                  # Main entry point for the library
├── core/                     # Core functionality
├── maven/                    # Maven integration
├── plugin/                   # Plugin management
├── settings/                 # Settings management
├── tool/                     # Tools functionality
└── ui/                       # UI components
```

## Core Components

### `Pluto.kt`
The main entry point for the library that initializes the debugger.

### Core Package (`core/`)
Contains core functionality for the Pluto debugger:

- **AppLifecycle**: Manages application lifecycle events
- **Network**: Handles network-related functionality
- **Notch**: Manages the debug notch UI element
- **Notification**: Handles debug notifications
- **Session**: Manages debugging sessions

### Maven Package (`maven/`)
Handles Maven integration:

- **MavenApiService**: Service for Maven API interactions
- **MavenSession**: Manages Maven sessions
- **MavenViewModel**: ViewModel for Maven-related UI

### Plugin Package (`plugin/`)
Manages plugins for the Pluto debugger:

- **PluginManager**: Manages plugin registration and lifecycle
- **PluginsViewModel**: ViewModel for plugin-related UI
- **Selector**: UI components for plugin selection

### Settings Package (`settings/`)
Handles settings for the Pluto debugger:

- **SettingsFragment**: UI for settings
- **SettingsViewModel**: ViewModel for settings
- **Various holders**: UI components for different settings

### Tool Package (`tool/`)
Manages debugging tools:

- **PlutoTool**: Base class for tools
- **ToolManager**: Manages tool registration and lifecycle
- **Modules**: Various debugging tools
  - **CurrentScreen**: Shows current screen information
  - **Grid**: Grid overlay tool
  - **Ruler**: Measurement tool
  - **ScreenHistory**: Screen navigation history

### UI Package (`ui/`)
Contains UI components:

- **Container**: Main container for Pluto UI
- **Selector**: UI for selecting plugins and tools
- **Custom views**: Reusable UI components

## Architecture

Pluto follows an MVVM (Model-View-ViewModel) architecture pattern:

1. **Models**: Data classes representing the state
2. **Views**: UI components (Activities, Fragments, ViewHolders)
3. **ViewModels**: Manage UI-related data and business logic

The plugin system allows for extensibility, with each plugin providing specific debugging functionality that can be integrated into the main Pluto interface.
