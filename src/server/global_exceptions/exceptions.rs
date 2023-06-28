use std::collections::HashMap;

#[derive(Debug)]
pub struct Exception<T, S> {
    exceptions: HashMap<T,S>
}