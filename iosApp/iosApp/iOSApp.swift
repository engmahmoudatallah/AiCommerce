import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        IosLiteRtBridge.shared.register(host: LiteRtEmbeddingHost.shared)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
