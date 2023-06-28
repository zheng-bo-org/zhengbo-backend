use crate::server::server::{Server};
use crate::web_frameworks::web::Web;
use log4rs;
use log::info;
pub mod server;
pub mod db;
pub mod web_frameworks;
use serde::{
    Deserialize,
    Serialize
};
use serde_json;

async fn init_log() {
    log4rs::init_file("config/log4rs.yaml", Default::default()).unwrap();

    info!("booting up");
}

#[derive(Debug, Deserialize)]
pub struct User {

}

async fn main() {
    init_log().await;
    // let user: User = User {
    //     token: "test".into(),
    //     username: "test".into(),
    //     pwd: "test".into(),
    // };
    //
    // print!("{}", routers::user::UserRouters::is_token_expired(&user));
    let server = &mut Server{
        port: 8080
    };
    server.start().await;
}