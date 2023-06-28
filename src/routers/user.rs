
#[derive(Debug)]
pub struct User<'a> {
    pub token: &'a str,
    pub username: &'a str,
    pub pwd: &'a str
}

pub trait UserRouters {
    fn is_token_expired(self: &Self) -> bool;
}

impl UserRouters for User<'_>  {
    fn is_token_expired(self: &Self) -> bool {
        self.token == "test"
    }
}