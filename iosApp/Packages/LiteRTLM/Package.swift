// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "LiteRTLM",
    platforms: [
        .iOS(.v15),
        .macOS(.v12),
    ],
    products: [
        .library(
            name: "LiteRTLM",
            targets: ["LiteRTLM"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "CLiteRTLM",
            path: "Prebuilt/CLiteRTLM.xcframework"
        ),
        .binaryTarget(
            name: "CLiteRTLM_mac",
            path: "Prebuilt/CLiteRTLM_mac.xcframework"
        ),
        .target(
            name: "LiteRTLM",
            dependencies: [
                .target(name: "CLiteRTLM", condition: .when(platforms: [.iOS])),
                .target(name: "CLiteRTLM_mac", condition: .when(platforms: [.macOS])),
            ],
            path: "swift",
            exclude: [
                "apple_fm",
                "ModelInfoTests.swift",
                "EngineTests.swift",
                "EmbeddingEngineTests.swift",
                "ConversationTests.swift",
                "ToolTests.swift",
                "MessageTests.swift",
                "BUILD",
                "Info.plist",
            ]
        ),
    ]
)
