#[derive(Debug)]
pub enum RouterMethod {
    GET,
    POST,
    DELETE,
    PUT,
    PATCH,
}

#[derive(Debug)]
pub struct Router<'a> {
    path: &'a String,
    method: &'a RouterMethod
}

type SetUp = fn(path: &String, method: &RouterMethod);
pub trait RouterActions {
    fn set_up(self: &Self, how_to_setup: SetUp);
}

impl RouterActions for Router<'_> {
    fn set_up(self: &Self, how_to_setup: SetUp) {
        how_to_setup(self.path, self.method);
    }
}