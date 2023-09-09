(ns zhengbo.backend.web.middleware.auth-test
  (:require [clojure.test :refer :all])
  (:require [zhengbo.backend.web.middleware.auth :refer [access-allowed?]]))

(deftest access-allowed?-test
  (testing "should return false")
  )
