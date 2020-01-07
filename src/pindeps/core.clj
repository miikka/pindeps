(ns pindeps.core
  (:refer-clojure :exclude [update])
  (:require [clojure.java.io :as io]
            [clojure.tools.deps.alpha :as deps]
            [clojure.tools.deps.alpha.reader :as deps.reader]
            [lambdaisland.deep-diff :as deep-diff])
  (:import [java.io FileNotFoundException]
           [org.apache.commons.codec.digest DigestUtils]))

(defn sha256 [file]
  (DigestUtils/sha256Hex (io/input-stream file)))

(defn checksum-dep
  [{:keys [paths] :as x}]
  {:pre [(or (= 1 (count paths))
             (prn x)
             false)]}
  (sha256 (first paths)))

(defn generate-checksums []
  (let [lib-map (-> (deps.reader/read-deps (deps.reader/default-deps))
                    (deps/resolve-deps nil))]
    (into {}
          (map (juxt key (comp checksum-dep val)))
          lib-map)))

(defn read-checksums []
  (try
    (read-string (slurp "pindeps.edn"))
    (catch FileNotFoundException _)))

(defn check []
  (let [generated (generate-checksums)
        stored (read-checksums)]
    (when (not= generated stored)
      (println "The checksums do not match.")
      (deep-diff/pretty-print (deep-diff/diff generated stored))
      (System/exit 1))))

(defn update []
  (spit "pindeps.edn" (generate-checksums)))

(defn -main [& [cmd]]
  (if (= "update" cmd)
    (update)
    (check)))
