use crate::db::db::DB;
use crate::server::global_exceptions::exceptions::Exception;
use std::{collections::HashMap};
use std::any::Any;
use std::fmt::Debug;

#[derive(Debug)]
pub enum RouterMethod {
    GET,
    POST,
    DELETE,
    PUT,
    PATCH,
}
#[derive(Debug)]
pub struct Request<> {
    pub current_user_id: String,
    pub body: HashMap<String, Box<dyn Any>>,
    pub headers: HashMap<String, String>,
}

#[derive(Debug)]
pub enum ErrType {
    Success,
    ServerError,
    BadRequest,
}

#[derive(Debug)]
pub struct Response<T> {
    pub body: T,
    pub err_type: ErrType,
    pub err_code: String,
    pub add_headers: HashMap<String, String>
}

pub struct Router<RT, SELF> {
    pub path: String,
    pub method: RouterMethod,
    pub router_handler: fn(body: Request, db: &DB, Exception<SELF>) -> Response<RT>
}