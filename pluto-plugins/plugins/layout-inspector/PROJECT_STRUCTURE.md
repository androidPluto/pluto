# Layout Inspector Plugin Structure

The Layout Inspector Plugin provides debugging capabilities for inspecting and manipulating UI layouts in Android applications. This document outlines the code structure of the Layout Inspector Plugin.

## Overview

The Layout Inspector Plugin is organized into the following structure:

```
com.pluto.plugins.layoutinspector/
├── PlutoLayoutInspectorPlugin.kt  # Main plugin class
├── BaseFragment.kt                # Base fragment for UI
├── ViewInfoFragment.kt            # Fragment for view information
└── internal/                      # Internal implementation
    ├── ActivityLifecycle.kt       # Activity lifecycle tracking
    ├── ParamsPreviewPanel.kt      # Layout parameters preview
    ├── attributes/                # View attributes handling
    ├── control/                   # UI controls
    ├── hierarchy/                 # View hierarchy
    ├── hint/                      # Hint system
    └── inspect/                   # Inspection functionality
```

## Core Components

### Main Plugin Classes

- **PlutoLayoutInspectorPlugin**: The main entry point for the plugin that integrates with Pluto.
- **BaseFragment**: Base fragment for the plugin UI.
- **ViewInfoFragment**: Fragment for displaying view information.

### Internal Implementation (`internal/`)

- **ActivityLifecycle**: Tracks activity lifecycle for inspection.
- **ParamsPreviewPanel**: UI component for previewing layout parameters.

#### Attributes (`internal/attributes/`)

- **ViewAttrFragment**: Fragment for displaying view attributes.
- **ViewAttrViewModel**: ViewModel for view attributes.
- **Data Models**: Classes for attribute representation.
  - **Attribute**: Base class for view attributes.
  - **MutableAttribute**: Base class for editable attributes.
  - **Mutability Types**: Specific attribute types (color, dimension, visibility, etc.).
- **Parsers**: Classes for parsing view attributes.
  - **AttributeParser**: Main parser for view attributes.
  - **Type-specific Parsers**: Parsers for specific view types (View, ViewGroup, TextView, ImageView).
- **List Components**: UI for displaying attributes.
  - **AttributeAdapter**: Adapter for displaying attributes.
  - **AttributeItemHolder**: ViewHolder for attribute items.
  - **AttributeTitleItemHolder**: ViewHolder for attribute section titles.

#### Controls (`internal/control/`)

- **ControlsWidget**: Widget for controlling the inspector.
- **ControlCta**: Call-to-action controls.
- **ControlCtaAdapter**: Adapter for control buttons.
- **ControlCtaItemHolder**: ViewHolder for control items.

#### Hierarchy (`internal/hierarchy/`)

- **ViewHierarchyFragment**: Fragment for displaying view hierarchy.
- **ViewHierarchyViewModel**: ViewModel for view hierarchy.
- **Hierarchy**: Utility for building view hierarchy.
- **NestedView**: Representation of nested views.
- **List Components**: UI for displaying hierarchy.
  - **HierarchyAdapter**: Adapter for displaying hierarchy.
  - **HierarchyItemHolder**: ViewHolder for hierarchy items.

#### Hint (`internal/hint/`)

- **HintFragment**: Fragment for displaying hints.
- **HintViewModel**: ViewModel for hints.
- **HintAdapter**: Adapter for displaying hints.
- **HintItemHolder**: ViewHolder for hint items.

#### Inspect (`internal/inspect/`)

- **InspectOverlay**: Overlay for inspecting views.
- **InspectViewModel**: ViewModel for inspection.
- **InspectedView**: Representation of an inspected view.
- **ViewUtilsKtx**: Utility extensions for views.
- **Canvas Components**: Drawing functionality.
  - **CaptureCanvas**: Canvas for capturing view state.
  - **DimensionCanvas**: Canvas for showing dimensions.
  - **GridCanvas**: Canvas for grid overlay.

## Resources

The plugin includes various resources:

- **Layouts**: UI layouts for the plugin screens and items.
- **Drawables**: Icons and backgrounds used in the UI.
- **Menus**: Menu resources for actions.
- **Navigation**: Navigation graph for the plugin.
- **Strings**: String resources used in the UI.
- **Styles**: Style resources for UI components.

## Architecture

The Layout Inspector Plugin follows the MVVM (Model-View-ViewModel) architecture pattern:

1. **Model**: Data classes representing view attributes and hierarchy.
2. **View**: UI components (Fragments, ViewHolders).
3. **ViewModel**: Manages UI-related data and business logic.

The plugin integrates with the main Pluto library through the Plugin interface defined in the Plugin Base module.

## Functionality

The Layout Inspector Plugin provides the following functionality:

1. **View Inspection**: Inspect views in the current activity.
2. **Hierarchy Visualization**: Display the view hierarchy.
3. **Attribute Viewing**: View all attributes of a selected view.
4. **Attribute Editing**: Modify view attributes in real-time.
5. **Layout Parameters**: View and modify layout parameters.
6. **Dimension Visualization**: Visualize view dimensions.
7. **Grid Overlay**: Display a grid for alignment.

This structure allows developers to easily inspect and debug UI layouts during app development.
