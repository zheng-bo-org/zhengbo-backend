use crate::server::router::Response;
use crate::server::router::{Request, Router};

use std::fmt::{Debug};

#[derive(Debug)]
pub struct Server {
    pub(crate) port: i32,
}

pub trait ServerActions {
    fn get_routers(
        self: &Self,
    ) -> Vec<Box<Router>>;
    fn get_middleware_on_request(
        self: &Self,
    ) -> Vec<fn(req: Request) -> Request>;

    fn get_middleware_on_response(
        self: &Self,
    ) -> Vec<fn(res: Response) -> Response>;
}

impl ServerActions for Server {
    fn get_routers(self: &Self) -> Vec<Box<Router>> {
        todo!()
    }

    fn get_middleware_on_request(self: &Self) -> Vec<fn(Request) -> Request> {
        todo!()
    }

    fn get_middleware_on_response(self: &Self) -> Vec<fn(Response) -> Response> {
        todo!()
    }
}

