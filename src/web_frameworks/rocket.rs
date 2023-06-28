use crate::server::server::{Server, ServerActions};
use crate::web_frameworks::web::Web;

impl Web for Server {
    async fn start(self: &Self) {
        let routers = self.get_routers();
        for router in routers  {
            //add my own router to Axum

        }
    }
}
