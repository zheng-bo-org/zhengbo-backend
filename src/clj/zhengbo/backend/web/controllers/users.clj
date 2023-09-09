(ns zhengbo.backend.web.controllers.users
  (:require [ring.util.http-response :as http-response])
  (:require [clojure.tools.logging :as log]))


(defn get-roles
  [req]
  (http-response/ok {:roles [:TEACHER :PARENT :STUDENT]}))