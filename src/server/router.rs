use crate::db::db::DB;
use crate::server::global_exceptions::exceptions::Exception;
use std::{collections::HashMap};
use std::any::Any;
use std::fmt::{Debug, Formatter, write};
use serde::{Serialize, Deserialize};
use serde_json;

#[derive(Debug)]
pub enum RouterMethod {
    GET,
    POST,
    DELETE,
    PUT,
    PATCH,
}

pub trait RequestActions {
    fn body_to_struct<'a, T: Deserialize<'a>>(self: &'a Self) -> T;
}

#[derive(Debug)]
pub struct Request {
    pub current_user_id: String,
    pub body: String,
    pub headers: HashMap<String, String>,
}

impl RequestActions for Request {
    fn body_to_struct<'a, T: Deserialize<'a>>(self: &'a Self) -> T {
        let the_struct: T = serde_json::from_str(&self.body).unwrap();
        return the_struct;
    }
}

#[derive(Debug)]
pub enum ErrType {
    Success,
    ServerError,
    BadRequest,
}

pub enum ResponseBodyContentType {
    ApplicationJson,
    Text,
}

pub trait ResponseBody {
    fn content_type(self: &Self) -> ResponseBodyContentType;
    fn to_json(self: &Self) -> String;
    fn to_text(self: &Self) -> String;
}

impl Debug for dyn ResponseBody {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "ResponseBody")
    }
}

#[derive(Debug)]
pub struct Response<> {
    pub body: Box<dyn ResponseBody>,
    pub err_type: ErrType,
    pub err_code: String,
    pub add_headers: HashMap<String, String>,
}

pub type Handler = fn(req: Request, db: &DB) -> Response;

pub struct Router {
    pub(crate) path: String,
    pub(crate) method: RouterMethod,
    pub(crate) router_handler: Handler,
}

impl Debug for Router {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "Router")
    }
}