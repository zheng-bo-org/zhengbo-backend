use crate::server::server::Server;
use crate::web_frameworks::web::Web;

pub mod web;
pub mod rocket;

impl Web for Server {
    async fn start(self: &Self) {
        todo!()
    }
}