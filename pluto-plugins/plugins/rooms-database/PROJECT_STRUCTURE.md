# Rooms Database Plugin Structure

The Rooms Database Plugin provides debugging capabilities for inspecting and manipulating Room databases in Android applications. This document outlines the code structure of the Rooms Database Plugin.

## Overview

The Rooms Database Plugin is organized into the following structure:

```
com.pluto.plugins.rooms.db/
├── PlutoRoomsDatabasePlugin.kt  # Main plugin class
├── PlutoRoomsDBWatcher.kt       # Database watcher
├── RoomsDBFragment.kt           # Main fragment
├── Session.kt                   # Session management
└── internal/                    # Internal implementation
    ├── ContentViewModel.kt      # ViewModel for content
    ├── DataModels.kt            # Data models
    ├── RoomsDBViewModel.kt      # Main ViewModel
    ├── UIViewModel.kt           # UI state ViewModel
    ├── core/                    # Core functionality
    └── ui/                      # UI components
```

## Core Components

### Main Plugin Classes

- **PlutoRoomsDatabasePlugin**: The main entry point for the plugin that integrates with Pluto.
- **PlutoRoomsDBWatcher**: Watches for Room database changes.
- **RoomsDBFragment**: Main fragment for the plugin UI.
- **Session**: Manages the plugin session state.

### Internal Implementation (`internal/`)

- **ContentViewModel**: ViewModel for database content.
- **DataModels**: Data models for database entities.
- **RoomsDBViewModel**: Main ViewModel for the plugin.
- **UIViewModel**: ViewModel for UI state management.

#### Core Functionality (`internal/core/`)

- **Utils**: Utility functions for database operations.
- **Query**: SQL query handling.
  - **Executor**: Executes SQL queries.
  - **Query**: Represents SQL queries.
- **Widgets**: Custom UI widgets.
  - **DataEditWidget**: Widget for editing data.
  - **TableGridView**: Grid view for displaying table data.

#### UI Components (`internal/ui/`)

- **ActionsFragment**: Fragment for row actions.
- **ColumnDetailsFragment**: Fragment for column details.
- **DetailsFragment**: Fragment for database details.
- **EditFragment**: Fragment for editing data.
- **QueryErrorFragment**: Fragment for displaying query errors.
- **SelectDBFragment**: Fragment for selecting a database.
- **SelectTableFragment**: Fragment for selecting a table.
- **TableSchemaFragment**: Fragment for displaying table schema.

##### Filter UI (`internal/ui/filter/`)

- **AddFilterConditionDialog**: Dialog for adding filter conditions.
- **ChooseColumnForFilterDialog**: Dialog for choosing columns to filter.
- **ChooseRelationDialog**: Dialog for choosing filter relations.
- **FilterConfig**: Configuration for filters.
- **FilterFragment**: Fragment for filtering data.
- **FilterQueryTransformer**: Transforms filters to SQL queries.

###### Filter List Components (`internal/ui/filter/list/`)

- **Column List**: Components for column selection.
- **Filter List**: Components for filter display.
- **Relation List**: Components for relation selection.

###### Filter Value Components (`internal/ui/filter/value/`)

- **ValueStubFactory**: Factory for value input components.
- **Components**: Various value input components.
  - **BaseValueStub**: Base class for value inputs.
  - **BetweenValueStub**: Input for BETWEEN operator.
  - **ComparisonValueStub**: Input for comparison operators.
  - **InValueStub**: Input for IN operator.
  - **LikeValueStub**: Input for LIKE operator.
  - **StringValueStub**: Input for string values.

##### List Components (`internal/ui/list/`)

- **Column List**: Components for displaying columns.
- **Database List**: Components for displaying databases.
- **Table List**: Components for displaying tables.

## Resources

The plugin includes various resources:

- **Layouts**: UI layouts for the plugin screens and items.
- **Drawables**: Icons and backgrounds used in the UI.
- **Menus**: Menu resources for actions.
- **Navigation**: Navigation graph for the plugin.
- **Colors**: Color resources.
- **Dimensions**: Dimension resources.
- **Strings**: String resources used in the UI.
- **Styles**: Style resources for UI components.

## Architecture

The Rooms Database Plugin follows the MVVM (Model-View-ViewModel) architecture pattern:

1. **Model**: Data classes representing database entities.
2. **View**: UI components (Fragments, ViewHolders).
3. **ViewModel**: Manages UI-related data and business logic.

The plugin integrates with the main Pluto library through the Plugin interface defined in the Plugin Base module.

## Functionality

The Rooms Database Plugin provides the following functionality:

1. **Database Inspection**: View all Room databases in the application.
2. **Table Inspection**: View tables within a database.
3. **Schema Viewing**: View the schema of tables.
4. **Data Viewing**: View the data in tables.
5. **Data Editing**: Edit, add, or delete rows in tables.
6. **Filtering**: Filter table data using various conditions.
7. **Custom Queries**: Execute custom SQL queries.
8. **Data Export**: Export table data.
9. **Table Operations**: Clear tables or perform other operations.

## Integration

The Rooms Database Plugin automatically detects Room databases in the application. No additional integration code is required beyond adding the plugin dependency to the project.

This structure allows developers to easily inspect and manipulate Room databases during app development, providing valuable insights into the application's data layer.
