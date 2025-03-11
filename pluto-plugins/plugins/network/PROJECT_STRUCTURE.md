# Network Plugin Structure

The Network Plugin provides debugging capabilities for monitoring and analyzing network requests and responses in Android applications. This document outlines the code structure of the Network Plugin.

## Overview

The Network Plugin is organized into the following structure:

```
com.pluto.plugins.network/
├── PlutoNetworkPlugin.kt       # Main plugin class
├── intercept/                  # Core interception functionality
├── interceptors/               # Framework-specific interceptors
│   ├── okhttp/                 # OkHttp interceptor
│   └── ktor/                   # Ktor interceptor
└── internal/                   # Internal implementation
    ├── CustomTabKtx.kt         # Tab extensions
    ├── DataModel.kt            # Data models
    ├── NetworkFragment.kt      # Main fragment
    ├── Session.kt              # Session management
    ├── database/               # Database functionality
    ├── interceptor/            # Interceptor implementation
    ├── mock/                   # Mock response functionality
    └── share/                  # Sharing functionality
```

## Core Components

### Main Plugin Class

- **PlutoNetworkPlugin**: The main entry point for the plugin that integrates with Pluto.

### Intercept Package (`intercept/`)

- **NetworkData**: Data models for network requests and responses.
- **NetworkInterceptor**: Core interceptor functionality.

### Interceptors Package (`interceptors/`)

#### OkHttp Interceptor (`interceptors/okhttp/`)

- **PlutoOkhttpInterceptor**: Interceptor for OkHttp.
- **PlutoOkhttpHelper**: Helper for OkHttp integration.
- **Internal Implementation**: Utilities for OkHttp interception.
  - **ContentProcessor**: Processes request/response content.
  - **DataConvertor**: Converts OkHttp data to plugin format.
  - **Utilities**: Stream handling utilities.

#### Ktor Interceptor (`interceptors/ktor/`)

- **PlutoKtorHelper**: Helper for Ktor integration.
- **Internal Implementation**: Utilities for Ktor interception.
  - **Converters**: Convert Ktor requests/responses to plugin format.

### Internal Implementation (`internal/`)

- **CustomTabKtx**: Extensions for custom tabs.
- **DataModel**: Data models for network calls.
- **NetworkFragment**: Main fragment for the plugin UI.
- **Session**: Manages the plugin session state.

#### Database (`internal/database/`)

- **DatabaseManager**: Manages database instances.
- **PlutoNetworkDatabase**: Room database definition.

#### Interceptor Implementation (`internal/interceptor/`)

##### Logic (`internal/interceptor/logic/`)

- **ContentProcessor**: Processes request/response content.
- **NetworkCallsRepo**: Repository for network calls.
- **ResponseCodeMessageMapper**: Maps response codes to messages.
- **ThrowableKtx**: Extensions for handling throwables.

###### Transformers (`internal/interceptor/logic/transformers/`)

- **BaseTransformer**: Base class for content transformers.
- **JsonTransformer**: Transforms JSON content.
- **XmlTransformer**: Transforms XML content.
- **FormEncodedTransformer**: Transforms form-encoded content.
- **AnyJsonAdapter**: JSON adapter for any type.

##### UI (`internal/interceptor/ui/`)

- **ContentFragment**: Fragment for displaying content.
- **DetailsFragment**: Fragment for request/response details.
- **ListFragment**: Fragment for displaying the list of network calls.
- **NetworkViewModel**: ViewModel for network UI.

###### Components (`internal/interceptor/ui/components/`)

- **CommonDetailsComponents**: Common UI components.
- **CustomTraceInfoFragment**: Fragment for trace information.
- **OverviewStub**: UI stub for overview.
- **RequestStub**: UI stub for request details.
- **ResponseStub**: UI stub for response details.

###### List (`internal/interceptor/ui/list/`)

- **NetworkAdapter**: Adapter for displaying network calls.
- **ApiItemHolder**: ViewHolder for API items.

#### Mock (`internal/mock/`)

- **Logic**: Repository and DAO for mock settings.
- **UI**: Fragments and ViewModels for mock settings.

#### Share (`internal/share/`)

- **ShareFragment**: Fragment for sharing network calls.
- **ShareHelper**: Helper for sharing functionality.
- **ShareOptionsAdapter**: Adapter for share options.
- **ShareOptionsViewModel**: ViewModel for share options.
- **Holders**: ViewHolders for share UI.

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

The Network Plugin follows the MVVM (Model-View-ViewModel) architecture pattern:

1. **Model**: Data classes representing network calls.
2. **View**: UI components (Fragments, ViewHolders).
3. **ViewModel**: Manages UI-related data and business logic.

The plugin integrates with the main Pluto library through the Plugin interface defined in the Plugin Base module.

## Functionality

The Network Plugin provides the following functionality:

1. **Network Monitoring**: Capture and display network requests and responses.
2. **Request Details**: View detailed information about requests (headers, body, etc.).
3. **Response Details**: View detailed information about responses (status, headers, body, etc.).
4. **Content Formatting**: Format and display JSON, XML, and other content types.
5. **Mock Responses**: Configure mock responses for specific requests.
6. **Sharing**: Share network call details for debugging.
7. **Filtering**: Filter network calls by URL, method, status, etc.
8. **Framework Support**: Support for OkHttp and Ktor HTTP clients.

## Integration

The Network Plugin can be integrated with different HTTP clients:

### OkHttp Integration

```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(PlutoOkhttpInterceptor())
    .build()
```

### Ktor Integration

```kotlin
val client = HttpClient {
    install(PlutoKtorHelper.KtorPlugin)
}
```

This structure allows developers to easily monitor and debug network calls during app development.
