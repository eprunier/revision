(ns revision.core
  (:require [revision.client.svn :as svn]))

(defn map-env []
  (reduce #(assoc % (.getKey %2) (.getValue %2)) {} (.entrySet (System/getProperties))))

(defn print-env []
  (let [env (map-env)]
    (println "proxySet : " (env "proxySet"))
    (println "http.proxyHost : " (env "http.proxyHost"))
    (println "http.proxyPort : " (env "http.proxyPort"))
    (println "https.proxyHost : " (env "https.proxyHost"))
    (println "https.proxyPort : "(env "https.proxyPort"))
    (println "http.nonProxyHosts : "(env "https.proxyPort"))))

(defn disable-proxy []
  (System/setProperty "proxySet" "false")
  (System/clearProperty "http.proxyHost")
  (System/clearProperty "http.proxyPort")
  (System/clearProperty "https.proxyHost")
  (System/clearProperty "https.proxyPort"))

(defmulti repository
  (fn [type _ _]
    (keyword type)))

(defmethod repository :svn [_ path options]
  (svn/get-repo path (:login options) (:password options)))

(defmethod repository :default [_ _ _]
  (println "WARNING:" type "not yet implementation"))
