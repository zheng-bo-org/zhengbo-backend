use crate::server::router::Response;
use crate::server::router::{Request, Router};
use std::collections::HashMap;
use std::fmt::{Debug, format};

#[derive(Debug)]
pub struct Server {
    pub(crate) port: i32,
}

#[derive()]
pub struct Req {
    pub body: Request<>,
}

pub struct Res<T> {
    pub response: Response<T>,
}

pub trait ServerActions {
    fn get_routers<RT: Debug, SELF: Debug>(
        self: &Self,
    ) -> Vec<Router<RT, SELF>>;
    fn get_middleware_on_request(
        self: &Self,
    ) -> Vec<fn(req: Req) -> Req>;
    fn get_middleware_on_response<T>(
        self: &Self,
    ) -> Vec<fn(res: Res<T>) -> Res<T>>;
}

impl ServerActions for Server {
    fn get_routers<RT: Debug, SELF: Debug>(self: &Self) -> Vec<Router<RT, SELF>> {
        todo!()
    }

    fn get_middleware_on_request(self: &Self) -> Vec<fn(Req) -> Req> {
        todo!()
    }

    fn get_middleware_on_response<T>(self: &Self) -> Vec<fn(Res<T>) -> Res<T>> {
        todo!()
    }
}

