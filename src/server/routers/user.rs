#[derive(Debug)]
pub struct User<'a> {
    pub(crate) token: &'a String,
    pub(crate) username: &'a String,
    pub(crate) pwd: &'a String
}

pub trait UserRouters {
    fn is_token_expired(self: &Self) -> bool;
}

impl UserRouters for User<'_>  {
    fn is_token_expired(self: &Self) -> bool {
        return self.token == "test"
    }
}