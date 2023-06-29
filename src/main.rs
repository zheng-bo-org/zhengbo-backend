use log4rs;
use log::info;
pub mod server;
pub mod db;
use serde::{
    Deserialize,
};
use axum::{
    routing::get,
    Router,
};
use crate::server::router_loader::RouterLoader;
use crate::server::routers::user::User;

async fn init_log() {
    log4rs::init_file("config/log4rs.yaml", Default::default()).unwrap();
}

#[tokio::main]
async fn main() {
    init_log().await;
    // build our application with a single route
    let mut app: Router = Router::new().route("/", get(|| async { "Hello, World!" }));

    let user_routers = User{};
    app = user_routers.load(app);

    // run it with hyper on localhost:3000
    axum::Server::bind(&"0.0.0.0:8080".parse().unwrap())
        .serve(app.into_make_service())
        .await
        .unwrap();
}