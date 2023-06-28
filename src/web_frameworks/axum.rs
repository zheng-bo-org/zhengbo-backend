use async_trait::async_trait;
use crate::server::server::{Server, ServerActions};
use axum::{
    routing::get,
    Router,
};
use crate::web_frameworks::web::Web;
use log::info;

// #[tokio::main]
// pub async fn start(server: &Server) {
//     let app = Router::new().route("/", get(|| async { "Hello, World!" }));

//     // run it with hyper on localhost:3000
//     axum::Server::bind(&"0.0.0.0:3000".parse().unwrap())
//         .serve(app.into_make_service())
//         .await
//         .unwrap();
// }

#[async_trait]
impl Web for Server {
    async fn start(self: &Self) {
        let app = Router::new().route("/", get(|| async {
            info!("Request comming...");
            return "Hello, World!";
        }));

        // run it with hyper on localhost:3000
        axum::Server::bind(&"0.0.0.0:3000".parse().unwrap())
            .serve(app.into_make_service())
            .await
            .unwrap();
    }
}