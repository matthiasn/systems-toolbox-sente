(ns matthiasn.systems-toolbox-sente.util)

(defn deserialize-meta
  "Helper function for deserializing the metadata from WebSockets message, as the metadata
  on the message would otherwise not have survived the serialization to EDN and back."
  [payload]
  (let [[cmd-type {:keys [msg msg-meta]}] payload]
    (with-meta [cmd-type msg] msg-meta)))

