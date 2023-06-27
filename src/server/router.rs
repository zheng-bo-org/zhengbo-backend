use crate::db::db::DB;
use crate::server::global_exceptions::exceptions::Exception;
use std::{collections::HashMap, fmt};
use std::fmt::{Debug, Formatter};

#[derive(Debug)]
pub enum RouterMethod {
    GET,
    POST,
    DELETE,
    PUT,
    PATCH,
}
#[derive(Debug)]
pub struct Body<'a, T> {
    current_user_id: &'a String,
    body: HashMap<&'a String, &'a T>,
    headers: HashMap<&'a String, &'a String>,
}

#[derive(Debug)]
pub enum ErrType {
    Success,
    ServerError,
    BadRequest,
}

#[derive(Debug)]
pub struct Response<'a, T> {
    body: &'a T,
    err_type: &'a ErrType,
    err_code: &'a String,
}

pub struct Router<'a, RT, BT, SELF> {
    pub path: &'a String,
    pub method: &'a RouterMethod,
    pub router_handler: fn(body: &Body<BT>, db: &DB, Exception<SELF>) -> Response<'a, RT>
}

impl<'a, RT, BT, SELF> Debug for Router<'a, RT, BT, SELF> {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        write!(f, "Router")
    }
}