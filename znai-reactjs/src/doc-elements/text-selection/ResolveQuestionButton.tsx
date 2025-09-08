/*
 * Copyright 2025 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, { useState, useEffect, useRef } from "react";
import "./ResolveQuestionButton.css";

interface Props {
  onClick(): void;
}

export function ResolveQuestionButton({ onClick }: Props) {
  const [isConfirming, setIsConfirming] = useState(false);
  const timeoutRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  const handleClick = () => {
    if (isConfirming) {
      onClick();
      setIsConfirming(false);
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
        timeoutRef.current = null;
      }
    } else {
      setIsConfirming(true);
      timeoutRef.current = setTimeout(() => {
        setIsConfirming(false);
        timeoutRef.current = null;
      }, 2000);
    }
  };

  return (
    <button className={`resolve-question-button ${isConfirming ? "confirming" : ""}`} onClick={handleClick}>
      {isConfirming ? "confirm" : "resolve"}
    </button>
  );
}
