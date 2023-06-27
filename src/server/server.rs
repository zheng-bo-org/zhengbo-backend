use std::fmt::Debug;
use crate::server::router::Router;
#[derive(Debug)]
pub struct Server {
    port: i32
}

pub struct Req {

}

pub struct Res {

}

type ReqMiddleWare = fn(req: &Req) -> bool;
type ResMiddleWare = fn(res: &Res) -> bool;
pub trait ServerActions {
    fn start(self: &Self) -> &Self;
    fn shutdown(self: &Self) -> &Self;
    fn register_routers<RT: Debug, BT: Debug, SELF: Debug>(self: &Self, routers: Vec<Router<RT, BT, SELF>>) -> &Self;
    fn add_middleware_on_request(self: &Self, middleware: ReqMiddleWare) -> &Self;
    fn add_middleware_on_response(self: &Self, middleware: ResMiddleWare) -> &Self;
}

impl ServerActions for Server {
    fn start(self: &Self) -> &Self {
        todo!()
    }

    fn shutdown(self: &Self) -> &Self {
        todo!()
    }

    fn register_routers<RT: Debug, BT: Debug, SELF: Debug>(self: &Self, routers: Vec<Router<RT, BT, SELF>>) -> &Self {
        todo!()
    }

    fn add_middleware_on_request(self: &Self, middleware: ReqMiddleWare) -> &Self {
        todo!()
    }

    fn add_middleware_on_response(self: &Self, middleware: ResMiddleWare) -> &Self {
        todo!()
    }
}